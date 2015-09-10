package moe.mzry.ilmare.service.data;

import java.util.Date;
import java.util.Objects;

/**
 * Placeholder class for the user message.
 */
public class BillboardMessage {
  private String id;
  private String content;
  private float x;
  private float y;
  private Date creationTime;

  public BillboardMessage(String id, String content, float x, float y) {
    this.id = id;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BillboardMessage)) {
      return false;
    }
    BillboardMessage that = (BillboardMessage) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
