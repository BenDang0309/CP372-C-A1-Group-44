import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.util.concurrent.*;

final class Conversation implements Runnable {
  private final Socket sock;
  private final Board board;
  private final BufferedReader in;
  private final PrintWriter out;
  private volatile boolean running = true; // purely for DISCONNECT command to set false

  // constructor
  public Conversation(Socket sock, Board board) throws IOException {
    this.sock = sock;
    this.board = board;
    this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    this.out = new PrintWriter(sock.getOutputStream(), true);
    sendHandshake();
  }

  // tells client the rules of the board
  private void sendHandshake() {
    out.println("BOARD " + board.getWidth() + " " + board.getHeight());
    out.println("NOTE " + board.getNoteW() + " " + board.getNoteH());
    out.println("COLORS " + String.join(",", board.getColors()));
  }

  @Override
  public void run() {
    try {
      String line;
      while (running && (line = in.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) { out.println("ERROR INVALID_FORMAT Empty command"); continue; }
        String cmd = line.split("\\s+", 2)[0].toUpperCase();

        // check command
        switch cmd {
          case "POST":
            handlePost(line);
            break;
          case "PIN":
            handlePin(line);
            break;
          case "UNPIN":
            handleUnpin(line);
            break;
          case "SHAKE":
            handleShake(line);
            break;
          case "CLEAR":
            handleClear(line);
            break;
          case "GET":
            handleGet(line);
            break;
          case "DISCONNECT":
            handleDisconnect();
            break;
          default:
            out.println("ERROR INVALID_FORMAT Unknown command: " + cmd);
        }
      }
    } catch (IOException e) {
    } finally {
      try { sock.close(); } catch (IOException ignored) {}
    }
  }

  // handles POST command
  private void handlePost(String line) {
    String parts[] = line.split("\\s+", 5);
    if (parts.length < 5) {
      out.println("ERROR INVALID_FORMAT POST requires \"POST x y color message\"");
      return;
    }
    try {
      int x = Integer.parseInt(parts[1]);
      int y = Integer.parseInt(parts[2]);
      String color = parts[3];
      String message = parts[4];
      String err = board.post(x, y, color, message);
      if (err == "OK") out.println("OK POSTED");
      else out.println(err);
    } catch (NumberFormatException e) {
      out.println("ERROR INVALID_FORMAT POST coordinates must be integers");
    }
  }

  // handles PIN command
  private void handlePin(String line) {
    String parts[] = line.split("\\s+");
    if (parts.length != 3) {
      out.println("ERROR INVALID_FORMAT PIN requires \"PIN x y\"");
      return;
    }
    try {
      int x = Integer.parseInt(parts[1]);
      int y = Integer.parseInt(parts[2]);
      int count = board.pin(x, y);
      if (count < 1) out.println("ERROR PIN_MISS No note inside those coordinates");
      else out.println("OK PIN_ADDED");
    } catch (NumberFormatException e) {
        out.println("ERROR INVALID_FORMAT PIN coordinates must be integers");
    }
  }

  // handles UNPIN command
  private void handleUnpin(String line) {
    String parts[] = line.split("\\s+");
    if (parts.length != 3) {
      out.println("ERROR INVALID_FORMAT UNPIN requires \"UNPIN x y\"");
      return;
    }
    try {
      int x = Integer.parseInt(parts[1]);
      int y = Integer.parseInt(parts[2]);
      int removed = board.unPinOne(x, y);
      if (removed == false) out.println("ERROR UNPIN_MISS No pin inside those coordinates");
      else out.println("OK PIN_REMOVED");
    } catch (NumberFormatException e) {
      out.println("ERROR INVALID_FORMAT UNPIN coordinates must be integers");
    }
  }
  
  // handles SHAKE command
  private void handleShake(String line) {
    board.shake();
    out.println("OK SHAKEN");
  }

  // handles CLEAR command
  private void handleClear(String line) {
    board.clear();
    out.println("OK CLEARED");
  }

  // handles GET command
  private void handleGet(String line) {
    String[] cmd = line.trim().split("\\s+");
    List<Note> notes = board.getNotes();
    
    // GET PINS
    if (cmd[1] = "PINS") {
      out.println("OK " + notes.size());
      for (Note n : notes) {
        out.println("PIN " + n.getX() + " " + n.getY());
      }
    }

    String color = null;
    int[] contains = null; // (x, y)
    String refersTo = null;    
    
    for (i = 0; i < cmd.length; i++) {
      String param = params[i];

      // handles color=
      if (param.startsWith("color=")) {
        color = param.substring(6);
      // handles contains=
      } else if (param.startsWith("contains=")) {
         String coords = param.substring(9);
        
        // if the coordinates are in the next index instead of this one
        if (raw.trim().isEmpty() && i + 1 < tokens.length) {
          coords = tokens[i++];
        }
        coords = coords.trim();
        String[] parts = raw.split("\\s+");
        
        if (parts.length != 2) {
          out.println("ERROR INVALID_FORMAT GET contains= requires <x> <y>");
          return;
        }

        try {
          contains = new int[]{Integer.parseInt(parts[0]),Integer.parseInt(parts[1])};
        } catch (NumberFormatException e) {
          out.println("ERROR INVALID_FORMAT GET contains= requires <x> <y>");
          return;
        }
      } else if (param.startsWith("refersTo")) {
        refersTo = param.substring("refersTo=".length());
      } else {
        out.println("ERROR INVALID_FORMAT GET accepts either PINS, or the optional parameters color, contains and refersTo");
        return;
      }
    }

    List<Note> result = new ArrayList<>();

    // filter notes
    for (Note n : notes) {
      // skip this note if there is a color specified but the note doesn't contain that color
      if (color != null && !n.getColor().equals(color)) {
        continue;
      }

      // skip this note if there is a coordinate specified but the note doesn't contain that coordinate
      if (contains != null) {
        if (!n.getX().equals(contains[0]) || !n.getY().equals(contains[1]) {
          continue;
        }
      }

      if (refersTo != null && !n.getMessage().contains(refersTo)) {
        continue;
      }
      result.add(n);
    }
    out.println("OK " + result.size());
    for (Note n : result) {
      out.println(n.formatted());
    }
  }

  // handle DISCONNECT command
  private void handleDisconnect() {
    running = false;
    out.println("OK DISCONNECTED");
    try { sock.close(); } catch (IOException ignored) {}
  }
}
  
