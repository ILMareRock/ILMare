package moe.mzry.ilmare.opengl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by yfchen on 9/3/15.
 */
public class Billboard implements Comparable<Billboard> {

  // Use to access and set the view transformation
  private int mMVPMatrixHandle;
  private FloatBuffer vertexBuffer;
  private ShortBuffer drawListBuffer;
  private FloatBuffer texBuffer;

  // number of coordinates per vertex in this array
  static final int COORDS_PER_VERTEX = 3;

  private float squareCoords[] = {0, 0, 0,
      0, 0, 0,
      0, 0, 0,
      0, 0, 0};
  private float texCoords[] = {
      0, 0,
      0, 1f,
      1f, 1f,
      1f, 0

  };
  private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

  private final int mProgram;
  private int mPositionHandle;
  private int mTexture;
  private int mTexCoord;
  private int mTexHandle;

  private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
  private float mX;
  private float mY;
  private float mCx;
  private float mCy;
  private float width = 1000; // real width / 2
  private static int textWidth = 1024;
  private int textHeight;
  private static int corner = 50;

  public Billboard(int program, String message, float x, float y) {
    this.mProgram = program;
    mX = x;
    mY = y;
    initShape();
    setText(message);
  }

  void initShape() {
    ByteBuffer bb = ByteBuffer.allocateDirect(
            // (# of coordinate values * 4 bytes per float)
            squareCoords.length * 4);
    bb.order(ByteOrder.nativeOrder());
    vertexBuffer = bb.asFloatBuffer();

    // initialize byte buffer for the draw list
    ByteBuffer dlb = ByteBuffer.allocateDirect(
            // (# of coordinate values * 2 bytes per short)
            drawOrder.length * 2);

    dlb.order(ByteOrder.nativeOrder());
    drawListBuffer = dlb.asShortBuffer();
    drawListBuffer.put(drawOrder);
    drawListBuffer.position(0);

    ByteBuffer tb = ByteBuffer.allocateDirect(
            // (# of coordinate values * 4 bytes per float)
            texCoords.length * 4);
    tb.order(ByteOrder.nativeOrder());
    texBuffer = tb.asFloatBuffer();
    texBuffer.put(texCoords);
    texBuffer.position(0);
  }

  public void setCamera(float x, float y) {
    this.mCx = x;
    this.mCy = y;
    resize();
  }

  private double dist() {
    float dx = mCx - mX;
    float dy = mCy - mY;
    return Math.sqrt(dx * dx + dy * dy);
  }

  private void resize() {float dx = mCx - mX;
    float dy = mCy - mY;
    double dist = Math.sqrt(dx * dx + dy * dy);
    dx /= dist;
    dy /= dist;

    float m = dx;
    dx = dy;
    dy = -m;

    float height = (float) textHeight / textWidth * width;
    squareCoords[0] = mX + dx * width;
    squareCoords[1] = mY + dy * width;
    squareCoords[2] = 500+height;
    squareCoords[3] = mX + dx * width;
    squareCoords[4] = mY + dy * width;
    squareCoords[5] = 500;
    squareCoords[6] = mX - dx * width;
    squareCoords[7] = mY - dy * width;
    squareCoords[8] = 500;
    squareCoords[9] = mX - dx * width;
    squareCoords[10] = mY - dy * width;
    squareCoords[11] = 500+height;

    vertexBuffer.put(squareCoords);
    vertexBuffer.position(0);
  }

  private void setText(String text) {
    TextPaint mTextPaint=new TextPaint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setTextSize(96);
    mTextPaint.setColor(Color.BLACK);
    StaticLayout layout = new StaticLayout(text, mTextPaint, textWidth - corner * 2,
        Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
    textHeight = layout.getHeight() + corner * 2;
    Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_4444);
    Canvas canvas = new Canvas(bitmap);

    RectF r = new RectF(0f, 0f, (float) textWidth, (float) textHeight);

    Paint bgPaint = new Paint();
    bgPaint.setAntiAlias(true);
    bgPaint.setARGB(0xAA, 0xCC, 0xFF, 0xCC);
    bgPaint.setStyle(Paint.Style.FILL);
    canvas.drawRoundRect(r, corner, corner, bgPaint);

    bgPaint.setStyle(Paint.Style.STROKE);
    bgPaint.setColor(Color.BLACK);
    bgPaint.setStrokeWidth(5);
    canvas.drawRoundRect(r, corner, corner, bgPaint);

    canvas.save();
    canvas.translate(corner, corner);
    layout.draw(canvas);
    canvas.restore();

    int texture[] = new int[1];
    GLES20.glGenTextures(1, texture, 0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_NEAREST);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE);

    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    mTexHandle = texture[0];
  }

  public void draw(float[] mvpMatrix) {
    float dx = mCx - mX;
    float dy = mCy - mY;
    double dist = Math.sqrt(dx * dx + dy * dy);
    if (dist < 1000) {
      // too near
      return;
    }

    // Add program to OpenGL ES environment
    GLES20.glUseProgram(mProgram);

    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexHandle);

    // get handle to vertex shader's vPosition member
    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

    // Enable a handle to the triangle vertices
    GLES20.glEnableVertexAttribArray(mPositionHandle);

    // Prepare the triangle coordinate data
    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer);

    // get handle to shape's transformation matrix
    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    // Pass the projection and view transformation to the shader
    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

    mTexture = GLES20.glGetUniformLocation(mProgram, "u_samplerTexture");
    mTexCoord = GLES20.glGetAttribLocation(mProgram, "vTexCoord");
    GLES20.glUniform1i(mTexture, 0);
    GLES20.glVertexAttribPointer(mTexCoord, 2,
            GLES20.GL_FLOAT, false,
            0, texBuffer);
    GLES20.glEnableVertexAttribArray(mTexCoord);

    // Draw the triangle
    GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.length,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

    // Disable vertex array
    GLES20.glDisableVertexAttribArray(mPositionHandle);
  }

  @Override
  public int compareTo(Billboard another) {
    double dist = dist();
    double anotherDist = another.dist();
    return dist < anotherDist ? -1 : (dist > anotherDist ? 1 : 0);
  }
}
