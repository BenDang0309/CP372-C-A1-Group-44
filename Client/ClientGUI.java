import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

public class ClientGUI extends JFrame {

    private final ProtocolClient client = new ProtocolClient();

    private JTextField hostField = new JTextField("127.0.0.1", 12);
    private JTextField portField = new JTextField("4554", 6);
    private JButton connectBtn = new JButton("Connect");
    private JButton disconnectBtn = new JButton("Disconnect");

    private JTextArea output = new JTextArea(14, 60);

    private JTextField postX = new JTextField("10", 5);
    private JTextField postY = new JTextField("10", 5);
    private JComboBox<String> postColor = new JComboBox<>();
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

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JPanel top = new JPanel();
        top.add(new JLabel("Host:"));
        top.add(hostField);
        top.add(new JLabel("Port:"));
        top.add(portField);
        top.add(connectBtn);
        top.add(disconnectBtn);

        JPanel postPanel = new JPanel();
        postPanel.setBorder(new TitledBorder("POST"));
        postPanel.add(new JLabel("x"));
        postPanel.add(postX);
        postPanel.add(new JLabel("y"));
        postPanel.add(postY);
        postPanel.add(postColor);
        postPanel.add(new JScrollPane(postMsg));
        postPanel.add(postBtn);

        JPanel pinPanel = new JPanel();
        pinPanel.setBorder(new TitledBorder("PIN / UNPIN"));
        pinPanel.add(new JLabel("x"));
        pinPanel.add(pinX);
        pinPanel.add(new JLabel("y"));
        pinPanel.add(pinY);
        pinPanel.add(pinBtn);
        pinPanel.add(unpinBtn);

        JPanel cmdPanel = new JPanel();
        cmdPanel.add(getBtn);
        cmdPanel.add(getPinsBtn);
        cmdPanel.add(shakeBtn);
        cmdPanel.add(clearBtn);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(postPanel);
        center.add(pinPanel);
        center.add(cmdPanel);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);

        wireActions();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void wireActions() {

        connectBtn.addActionListener(e -> {
            try {
                ProtocolClient.Handshake h =
                        client.connect(hostField.getText(), Integer.parseInt(portField.getText()));
                output.append("Connected\n");
                postColor.removeAllItems();
                for (String c : h.colors) postColor.addItem(c);
            } catch (Exception ex) {
                output.append("Connect error: " + ex.getMessage() + "\n");
            }
        });

        disconnectBtn.addActionListener(e -> {
            try {
                client.disconnect();
                output.append("Disconnected\n");
            } catch (IOException ignored) {}
        });

        postBtn.addActionListener(e ->
                send("POST " + postX.getText() + " " + postY.getText() + " " +
                        postColor.getSelectedItem() + " " + postMsg.getText()));

        pinBtn.addActionListener(e ->
                send("PIN " + pinX.getText() + " " + pinY.getText()));

        unpinBtn.addActionListener(e ->
                send("UNPIN " + pinX.getText() + " " + pinY.getText()));

        getBtn.addActionListener(e -> send("GET"));
        getPinsBtn.addActionListener(e -> send("GET PINS"));
        shakeBtn.addActionListener(e -> send("SHAKE"));
        clearBtn.addActionListener(e -> send("CLEAR"));
    }

    private void send(String cmd) {
        try {
            output.append(">> " + cmd + "\n");
            ProtocolClient.Response r = client.sendCommand(cmd);
            output.append("<< " + r.firstLine + "\n");
            for (String line : r.payload)
                output.append("<< " + line + "\n");
        } catch (Exception e) {
            output.append("ERROR: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
}
