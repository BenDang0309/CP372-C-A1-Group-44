import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.util.concurrent.*;

public final class Server {
  // Java concurrenthashmap for multi-threading
  public static ConcurrentHashMap<String, Note> notes = new ConcurrentHashMap<>(); // stores the notes by their coordinates
  
  public static void main(String argv[]) {
    int port = 1000;
    if (argv.length > 0) {
			try {
				port = Integer.valueOf(argv[0]);
			}
			catch (NumberFormatException e) {
				System.out.println("invalid port, defaulting to 1000");
			}
		}
		else {
			System.out.println("User didn't enter port, defaulting to 1000");
		}

    System.out.println("Server starting in port number: " + port);
    
  }
}
