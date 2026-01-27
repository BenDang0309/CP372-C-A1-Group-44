import java.ulti.*;

public class Board {
  private final int boardW;
  private final int boardH;
  private final Set<String> colors;
  private final List<Note> notes;
  
  public Board(int boardW, int boardH, int noteW, int noteH, List<String> colors) {
    this.boardW = boardW;
    this.boardH = boardH;
    this.noteW = noteW;
    this.noteH = noteH;
    this.colors = new Set<>(colors);
    this.notes = new ArrayList<>();
  }
}
