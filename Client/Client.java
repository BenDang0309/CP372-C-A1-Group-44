import java.io.* ;
import java.net.* ;
import javax.swing.SwingUtilities;

public class Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String sendCommand(String command) throws IOException {
        out.println(command);
        return in.readLine();
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
    }
}
