import java.util.*;

public class Board {
  private final int boardW;
  private final int boardH;
  private final Set<String> colors;
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
    this.notes = new ArrayList<>();
  }
}

// check if input color is in the set
public boolean isValidColor(String color) {
  if (color == null) return False;
  return colors.contains(color.trim().toLowerCase());
}

// get list of all notes to iterate through for GET
public synchronized List<Note> getNotes() {
  return new ArrayList<>(notes);
}

// checks for errors, then post note into board
public synchronized String post(int x, int y, String color, String message) {
  if (x < 0 || y < 0 || x + noteW > boardW || y + noteH > boardH) return "ERROR OUT_OF_BOUNDS";
  if (!isValidColor(color)) return "ERROR COLOR_NOT_SUPPORTED";
  for (Note n : notes) {
    if (n.overlap(x, y, noteW, noteH)) {
      return "ERROR COMPLETE_OVERLAP";
    }
  }
  Note created = new Note(x, y, noteW, noteH, color.trim().toLowerCase(), standardize(message));
  notes.add(created);
  return "OK"
}

// pins all notes containing coordinates (x, y), then returns notes pinned
public synchronized int pin(int x, int y){
  int count = 0;
  for (Note n : notes) {
    if (n.containsPoint(x, y)) {
      n.addPin(x, y);
      count++;
    }
  }
  return count;
}

// unpins one pin, as stated in the specifications. Return true if successful, false otherwise
public synchronized boolean unPinOne(int x, int y){
  for (Note n : notes) {
    if (n.containsPoint(x, y) && n.unPin(x, y)) {
      return True;
    }
  }
  return False;
}

// removes all unpinned notes
public synchronized void shake(){
  Iterator<Note> i = notes.iterator();
  while (i.hasNext()) {
    Note n = i.next();
    if (!n.isPinned()) {
      i.remove();
    }
  }
}

// removes all notes and their respective pins alongside them
public synchronized void clear() {
  notes.clear();
}

// removes leading/trailing spaces and newlines 
private String standardize(String msg) {
  if (msg == null) return "";
  return msg.replaceAll("[\\r\\n]+", " ").trim();
}

// getters
public int getNoteW() { return noteW; }
public int getNoteH() { return noteH; }
