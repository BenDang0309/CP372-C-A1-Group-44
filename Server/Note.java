import java.util.* ;

public class Note {
  public final int color;
  public final String message;
  public final int x;
  public final int y;
  public boolean pinned;

  public Note(int x, int y, String color, String message) {
    this.color = color;
    this.message = message;
    this.x = x;
    this.y = y;
    this.pinned = pinned;
  }
}
