package moe.mzry.ilmare.service.data;

import java.util.Date;

/**
 * Placeholder class for the user message.
 */
public class BillboardMessage {
  private String content;
  private float x;
  private float y;
  private Date creationTime;

  public BillboardMessage(String content, float x, float y) {
    this.content = content;
    this.x = x;
    this.y = y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public String getContent() {
    return content;
  }
}
