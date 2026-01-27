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
}
