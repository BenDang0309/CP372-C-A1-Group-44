import java.ulti.*;

public class Board {
  private final int boardW;
  private final int boardH;
  private final Set<String> colors;
  private final String initLine;
  private final List<Note> notes;
  
  public Board(int boardW, int boardH, int noteW, int noteH, List<String> colors) {

    if (boardW <= 0 || boardH <= 0 || noteW <= 0 || noteH <= 0) {
      throw new IllegalArgumentException("Dimensions must be > 0");
    }
    if (noteW > boardW || noteH > boardH) {
      throw new IllegalArgumentException("Note dimensions must fit inside board dimensions");
    }
    if (colors == null || colors.isEmpty()) {
      throw new IllegalArgumentException("At least one color required");
    }
    
    this.boardW = boardW;
    this.boardH = boardH;
    this.noteW = noteW;
    this.noteH = noteH;
    this.colors = new Set<>(colors);

    for (i = 0; i < colors.size(); i++){
      colors.add(colors(i).trim().toLowerCase())
    }

    // initialization format
    this.initLine = "INIT " + boardW + " " + boardH + " " + noteW + " " + noteH + " " + String.join(",", colors);
    
    this.notes = new ArrayList<>();
  }
}

// check if input color is in the set
public boolean isValidColor(String color) {
  if (color == null) return False;
  return colors.contains(color.trim().toLowerCase());
}

public List<String> getNotes() {
  List<String> result = new ArrayList<>(notes.size());
  for (Note n : notes) result.add(n.formatted());
  return result;
}

// getters
public string getInitLine { return initLine; }
public int getNoteW() { return noteW; }
public int getNoteH() { return noteH; }
