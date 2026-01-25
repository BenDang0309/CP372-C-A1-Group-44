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

    ExecutorService pool = Executors.newCachedThreadPool(); // pool of threads

    try (ServerSocket socket = new ServerSocket(port)) {
        System.out.println("Server starting at port " + port);
        // process services
        while (true) {
            try {
                Socket connection = socket.accept(); // listen for TCP connection request
                pool.submit(new Conversation(connection)); // runs the conversation task on one of the threads in the pool
            }
            catch (IOException acceptEx) {
                System.err.println("Accept failed: " + acceptEx.getMessage());
            }
        }
    } catch (IOException e) {
        System.err.println("Could not start server on port " + port + ": " + e.getMessage());
    } finally {
        pool.shutdown();
    }
  }
}
