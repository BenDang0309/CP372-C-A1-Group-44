import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.util.concurrent.*;

final class Conversation implements Runnable {
  Socket socket;
  ConcurrentHashMap<String, Note> notes;

  // constructor
  public Conversation(Socket socket, ConcurrentHashMap<String, Note> notes) {
    this.socket = socket;
    this.notes = notes;
  }
  
  public void run() {
    try {
      process();
    }
    catch (Exception e) {
      println("Exception: " + e);
    }
  }

  private void process() {
    System.out.println("Processing request");
    
    Scanner input = new Scanner(socket.getInputStream()); // take in input
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // write out output
    String cmd = input.nextLine(); // command
    String parts[] = cmd.split(" ", 5); // splitting the command up into parts for processing
    String op = parts[0]; // the operation

    System.out.println("Operation: " + op);

    if (op.equals("POST") {
      int x = Integer.parseInt(parts[1]);
      int y = Integer.parseInt(parts[2]);
      String color = parts[3];
      String message = parts[4];
      
    }
    
  }
}
