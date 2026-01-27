import java.util.* ;

class Pin {
  public final int x;
  public final int y;

  public Pin(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

public class Note {
  public final String color;
  public final String message;
  public final int x;
  public final int y;
  public final int width;
  public final int height;
  private List<Pin> pins;

  public Note(int x, int y, int width, int height, String color, String message) {
    this.color = color;
    this.message = message;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.pins = new ArrayList<>();
  }

  // get number of pins
  public synchronized int getPinCount() {
    return pins.size();
  }
  
  // check if there is a pin in given coordinates. Returns the position in list if there is, else -1
  public synchronized int isTherePin(int px, int py) {
    for (int i = 0; i < pins.size(); i++) {
      Pin p = pins.get(i);
      if (p.x == px && p.y == py) { 
        return i; // found it      
      }
    }
    return -1; // there wasn't a pin there
  }
  
  // add pin
  public synchronized boolean addPin(int px, int py) {
    int i = isTherePin(px, py);
    if (i == -1) {
      pins.add(new Pin(px, py));
      return true;
    }
    return false;
  }

  // remove pin but check if there is a pin to remove
  public synchronized boolean unPin(int ux, int uy) {
    int i = isTherePin(ux, uy);
    if (i != -1) {
      pins.remove(i);
      return true;
    }
    return false; // there wasn't a pin there
  }

  // check if there is a pin, for shake
  public synchronized boolean isPinned() {
    return getPinCount() > 0;
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
  public String getColor() { return color; }
  public String getMessage() { return message; }
  public int getX() { return x; }
  public int getY() { return y; }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  
}
