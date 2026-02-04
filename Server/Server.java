import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public final class Server {
    public static void main(String[] args) {
        // format is <port> <board_width> <board_height> <note_width> <note_height> <color1> <color2>...<colorN>
        if (args.length < 6) {
            System.err.println("wrong format, less than 6 inputs");
            System.exit(1);
        }

        ExecutorService pool = null;
        ServerSocket serverSocket = null;
        int port = -1;

        try {
            // seperate the input into pieces
            final int port = Integer.parseInt(args[0]);
            final int bw = Integer.parseInt(args[1]);
            final int bh = Integer.parseInt(args[2]);
            final int nw = Integer.parseInt(args[3]);
            final int nh = Integer.parseInt(args[4]);
            final List<String> colors = new ArrayList<>();
            for (int i = 5; i < args.length; i++) colors.add(args[i]);

            // create the main board
            final Board board = new Board(bw, bh, nw, nh, colors);

            // thread pool
            ExecutorService pool = Executors.newCachedThreadPool();
          
            // socket
            ServerSocket socket = new ServerSocket(port);
            System.out.println("Server starting at port " + port);

            // process services
            while (true) {
                Socket connection = null
                try {
                    connection = socket.accept();
                    Conversation conv = new Conversation(connection, board);
                    pool.submit(conv);
                } catch (RuntimeException rte) {
                    System.err.println("Failed to start conversation: " + rte.getMessage());
                    if (connection != null && !connection.isClosed()) {
                        try { connection.close(); } catch (IOException ignored) {}
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Bad numeric argument: " + e.getMessage());
        } catch (IOException ioe) {
            System.err.println("Could not start server on port " + port + ": " + ioe.getMessage());
        } finally {
            if (pool != null) pool.shutdown();
            if (serverSocket != null && !serverSocket.isClosed()) {
                try { serverSocket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
