// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class ClientGUI extends JFrame {
   private final ProtocolClient client = new ProtocolClient();
   private JTextField hostField = new JTextField("127.0.0.1", 12);
   private JTextField portField = new JTextField("4554", 6);
   private JButton connectBtn = new JButton("Connect");
   private JButton disconnectBtn = new JButton("Disconnect");
   private JTextArea output = new JTextArea(14, 60);
   private JTextField postX = new JTextField("10", 5);
   private JTextField postY = new JTextField("10", 5);
   private JComboBox<String> postColor = new JComboBox();
   private JTextArea postMsg = new JTextArea(3, 40);
   private JButton postBtn = new JButton("POST");
   private JTextField pinX = new JTextField("15", 5);
   private JTextField pinY = new JTextField("12", 5);
   private JButton pinBtn = new JButton("PIN");
   private JButton unpinBtn = new JButton("UNPIN");
   private JButton getBtn = new JButton("GET");
   private JButton getPinsBtn = new JButton("GET PINS");
   private JButton shakeBtn = new JButton("SHAKE");
   private JButton clearBtn = new JButton("CLEAR");

   public ClientGUI() {
      super("Bulletin Board Client");
      this.output.setEditable(false);
      this.output.setFont(new Font("Monospaced", 0, 12));
      JPanel var1 = new JPanel();
      var1.add(new JLabel("Host:"));
      var1.add(this.hostField);
      var1.add(new JLabel("Port:"));
      var1.add(this.portField);
      var1.add(this.connectBtn);
      var1.add(this.disconnectBtn);
      JPanel var2 = new JPanel();
      var2.setBorder(new TitledBorder("POST"));
      var2.add(new JLabel("x"));
      var2.add(this.postX);
      var2.add(new JLabel("y"));
      var2.add(this.postY);
      var2.add(this.postColor);
      var2.add(new JScrollPane(this.postMsg));
      var2.add(this.postBtn);
      JPanel var3 = new JPanel();
      var3.setBorder(new TitledBorder("PIN / UNPIN"));
      var3.add(new JLabel("x"));
      var3.add(this.pinX);
      var3.add(new JLabel("y"));
      var3.add(this.pinY);
      var3.add(this.pinBtn);
      var3.add(this.unpinBtn);
      JPanel var4 = new JPanel();
      var4.add(this.getBtn);
      var4.add(this.getPinsBtn);
      var4.add(this.shakeBtn);
      var4.add(this.clearBtn);
      JPanel var5 = new JPanel();
      var5.setLayout(new BoxLayout(var5, 1));
      var5.add(var2);
      var5.add(var3);
      var5.add(var4);
      this.setLayout(new BorderLayout());
      this.add(var1, "North");
      this.add(var5, "Center");
      this.add(new JScrollPane(this.output), "South");
      this.wireActions();
      this.setDefaultCloseOperation(3);
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   private void wireActions() {
      this.connectBtn.addActionListener((var1) -> {
         try {
            ProtocolClient$Handshake var2 = this.client.connect(this.hostField.getText(), Integer.parseInt(this.portField.getText()));
            this.output.append("Connected\n");
            this.postColor.removeAllItems();
            Iterator var3 = var2.colors.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               this.postColor.addItem(var4);
            }
         } catch (Exception var5) {
            this.output.append("Connect error: " + var5.getMessage() + "\n");
         }

      });
      this.disconnectBtn.addActionListener((var1) -> {
         try {
            this.client.disconnect();
            this.output.append("Disconnected\n");
         } catch (IOException var3) {
         }

      });
      this.postBtn.addActionListener((var1) -> {
         String var10001 = this.postX.getText();
         this.send("POST " + var10001 + " " + this.postY.getText() + " " + String.valueOf(this.postColor.getSelectedItem()) + " " + this.postMsg.getText());
      });
      this.pinBtn.addActionListener((var1) -> {
         String var10001 = this.pinX.getText();
         this.send("PIN " + var10001 + " " + this.pinY.getText());
      });
      this.unpinBtn.addActionListener((var1) -> {
         String var10001 = this.pinX.getText();
         this.send("UNPIN " + var10001 + " " + this.pinY.getText());
      });
      this.getBtn.addActionListener((var1) -> {
         this.send("GET");
      });
      this.getPinsBtn.addActionListener((var1) -> {
         this.send("GET PINS");
      });
      this.shakeBtn.addActionListener((var1) -> {
         this.send("SHAKE");
      });
      this.clearBtn.addActionListener((var1) -> {
         this.send("CLEAR");
      });
   }

   private void send(String var1) {
      try {
         this.output.append(">> " + var1 + "\n");
         ProtocolClient$Response var2 = this.client.sendCommand(var1);
         this.output.append("<< " + var2.firstLine + "\n");
         Iterator var3 = var2.payload.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            this.output.append("<< " + var4 + "\n");
         }
      } catch (Exception var5) {
         this.output.append("ERROR: " + var5.getMessage() + "\n");
      }

   }

   public static void main(String[] var0) {
      SwingUtilities.invokeLater(() -> {
         (new ClientGUI()).setVisible(true);
      });
   }
}
