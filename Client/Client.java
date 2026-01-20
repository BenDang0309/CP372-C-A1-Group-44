import java.io.* ;
import java.net.* ;
import javax.swing.SwingUtilities;

public class Client {
  private static final int PORT = 5000;

  private static Jlabel errorLabel
  public int x, y;
  public String color;
  public String message;
  public boolean pinned;

  public static void main(String[] argv) {
    SwingUtilities.invokeLater(() -> {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            host = "localhost";  
            System.err.println("Could not determine local host address: " + e.getMessage());
        }
        openGUI();
    });
    
  }
  
}
