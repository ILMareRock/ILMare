package moe.mzry.ilmare.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.util.Pair;
import android.util.SizeF;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import moe.mzry.ilmare.service.DataModel;
import moe.mzry.ilmare.service.data.BillboardMessage;
import moe.mzry.ilmare.service.data.FirebaseLatLng;

/**
 * Created by yfchen on 9/3/15.
 */
public class GLRenderer implements GLSurfaceView.Renderer {
  private final String vertexShaderCode =
      "uniform mat4 uMVPMatrix;" +
          "attribute vec4 vPosition;" +
          "attribute vec2 vTexCoord;" +
          "varying vec2 v_texCoord;" +
          "void main() {" +
          "  gl_Position = uMVPMatrix * vPosition;" +
          "  v_texCoord = vTexCoord;" +
          "}";

  private final String fragmentShaderCode =
      "precision mediump float;" +
          "varying vec2 v_texCoord;" +
          "uniform sampler2D u_samplerTexture;" +
          "void main() {" +
          "  gl_FragColor = texture2D(u_samplerTexture, v_texCoord);" +
          "}";
  private HashMap<BillboardMessage, Billboard> billboardMessages = new HashMap<>();
  private Set<BillboardMessage> messagesToAdd = new HashSet<>();
  private final float[] mMVPMatrix = new float[16];
  private final float[] mProjectionMatrix = new float[16];
  private float mFocalLength = 4f;
  private int mViewWidth = 0;
  private int mViewHeight = 0;
  private int mCanvasWidth = 0;
  private int mCanvasHeight = 0;
  private SizeF mSensorSize;
  private float[] mRotationMat = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
  };
  private boolean ready = false;
  private int mProgram;
  private FirebaseLatLng mOrigin = null;

  String[] messages = {
      "Let me say something",
      "It is just a test message",
      "Just like other ones",
      "Some messages are relatively long, sine I want to see how this view works to fit a long message",
      "Others are not",
      "I think it is quite cool",
      "To render messages in 3d spaces",
      "Well actually it's 2-d",
      "I removed z-information",
      "To avoid seeing messages on the other plane",
      "Note that we support virtual planes",
      "It is very likely that we have to fake our position",
      "Using virtual position rather than physical ones",
      "Such as latitude, altitude",
      "Is much easier for debugging use",
      "At least in my case.",
      "Well that's almost all that I want to say",
      "Please stay tuned, this is not a final version",
      "And this UI is not reviewed by our designer",
      " - Ivan",
  };

  public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    // Set the background frame color
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GLES20.glEnable(GLES20.GL_TEXTURE_2D);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    mProgram = initShader();

    createBillboards();
  }

  int initShader() {
    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
        vertexShaderCode);
    int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
        fragmentShaderCode);
    // create empty OpenGL ES Program
    int program = GLES20.glCreateProgram();
    GLES20.glAttachShader(program, vertexShader);
    GLES20.glAttachShader(program, fragmentShader);
    GLES20.glLinkProgram(program);
    GLES20.glDeleteShader(vertexShader);
    GLES20.glDeleteShader(fragmentShader);
    return program;
  }

  private static int loadShader(int type, String shaderCode){
    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
    int shader = GLES20.glCreateShader(type);

    // add the source code to the shader and compile it
    GLES20.glShaderSource(shader, shaderCode);
    GLES20.glCompileShader(shader);

    return shader;
  }

  public void onSurfaceChanged(GL10 unused, int width, int height) {
    GLES20.glViewport(0, 0, width, height);
    this.mCanvasWidth = width;
    this.mCanvasHeight = height;
  }

  public void setFocalLength(float focalLength) {
    this.mFocalLength = focalLength;
  }

  public void setViewSize(int width, int height) {
    this.mViewWidth = width;
    this.mViewHeight = height;
  }

  public void setSensorSize(SizeF sensorSize) {
    this.mSensorSize = sensorSize;
  }

  public void setRotationMat(float[] rotationMat) {
    this.mRotationMat = rotationMat;
  }

  public void setOrigin(FirebaseLatLng location) {
    this.mOrigin = location;
  }

  public void setReady(boolean ready) {
    this.ready = ready;
  }

  public synchronized void setBillboards(Set<BillboardMessage> billboards) {
    Set<BillboardMessage> messagesToDelete = new HashSet<>();

    for (BillboardMessage b : billboardMessages.keySet()) {
      if (!billboards.contains(b)) {
        messagesToDelete.add(b);
      }
    }
    synchronized (messagesToAdd) {
      for (BillboardMessage b : billboards) {
        if (!billboardMessages.containsKey(b)) {
          messagesToAdd.add(b);
        }
      }
    }
    for (BillboardMessage b : messagesToDelete) {
      billboards.remove(b);
    }
  }

  private void createBillboards() {
    synchronized (messagesToAdd) {
      if (! messagesToAdd.isEmpty()) {
        for (BillboardMessage b : messagesToAdd) {
          billboardMessages.put(b, new Billboard(b.getContent(), b.getX(), b.getY()));
        }
        messagesToAdd.clear();
      }
    }
  }

  public void onDrawFrame(GL10 unused) {
    // Redraw background color
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    createBillboards();

    if (!ready || billboardMessages.isEmpty()) {
      return;
    }
    GLES20.glViewport(mCanvasWidth - mViewWidth, mCanvasHeight - mViewHeight, mViewWidth, mViewHeight);

    float h = mSensorSize.getWidth();
    float w = mSensorSize.getHeight();
    Matrix.frustumM(mProjectionMatrix, 0, -w / 2, w / 2, -h / 2, h / 2, mFocalLength, 1e10f);

    // Calculate the projection and view transformation
    Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mRotationMat, 0);

    // Draw shape
    Pair<Double, Double> camera;
    if (mOrigin == null) {
      camera = Pair.create(0d, 0d);
    } else {
      camera = mOrigin.relativeDistance(DataModel.INSTANCE.getCurrentLocation());
    }
    for (Billboard b : billboardMessages.values()) {
      b.setCamera(camera.first.floatValue(), camera.second.floatValue());
    }

    Billboard[] mBillboard = billboardMessages.values().toArray(new Billboard[0]);

    Arrays.sort(mBillboard);
    GLES20.glDepthMask(false);
    for (int i = mBillboard.length - 1; i >= 0; i--) {
      mBillboard[i].draw(mProgram, mMVPMatrix);
    }
    GLES20.glDepthMask(true);
  }
}
