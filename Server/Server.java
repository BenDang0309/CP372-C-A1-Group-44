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
	// listen socket
	ServerSocket socket;
	try { 
		socket = new ServerSocket(port);
	}
	catch (IOException e) {
		System.out.println("User didn't enter port, defaulting to 1000");
		port = 1000;
		socket = new ServerSocket(port);
	}

	// infinite loop to process services
	while (true) {
		Socket connection = socket.accept(); // listen for TCP connection request
		Conversation request = new Conversation(connection,notes); // connection between client and server
		Thread thread = new Thread(request); // new thread to process the request
		thread.start();
  	}
}
