import java.util.* ;

public class Note {
  public final String color;
  public final String message;
  public final int x;
  public final int y;
  public final int width;
  public final int height;
  private int pinCount;

  public Note(int x, int y, int width, int height, String color, String message) {
    this.color = color;
    this.message = message;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.pinCount = 0;
  }

  // add pin
  public synchronized void pin() {
    pinCount++;
  }

  // remove pin but check if there is a pin to remove
  public synchronized boolean unpin() {
    if (pinCount == 0) {
      return false;
    }
    pinCount--;
    return true;
  }

  // get number of pins
  public synchronized int getPinCount() {
    return pinCount;
  }

  // check if there is a pin, for shake
  public synchronized boolean isPinned() {
    return pinCount > 0;
  }

  // check if a note completely overlaps with another note
  public boolean overlap(int ox, int oy, int ow, int oh) {
    return this.x == ox && this.y == oy && this.width == ow && this.height == oh;
  }

  // check whether the point is inside the note coordinates, for pin/unpin
  public boolean containsPoint(int cx, int cy) {
    return (cx >= x) && (cx < x + width) && (cy >= y) && (cy < y + height);
  }

  // getters
  public String getId() { return id; }
  public String getColor() { return color; }
  public String getMessage() { return message; }
  public int getX() { return x; }
  public int getY() { return y; }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  
}
