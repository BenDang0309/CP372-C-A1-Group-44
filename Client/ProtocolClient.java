import java.io.*;
import java.net.*;
import java.util.*;

public class ProtocolClient {

    public static class Handshake {
        public final int boardW, boardH;
        public final int noteW, noteH;
        public final List<String> colors;

        public Handshake(int bw, int bh, int nw, int nh, List<String> colors) {
            this.boardW = bw;
            this.boardH = bh;
            this.noteW = nw;
            this.noteH = nh;
            this.colors = colors;
        }
    }

    public static class Response {
        public final boolean ok;
        public final String firstLine;
        public final List<String> payload;

        public Response(boolean ok, String firstLine, List<String> payload) {
            this.ok = ok;
            this.firstLine = firstLine;
            this.payload = payload;
        }
    }

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Handshake handshake;

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public Handshake connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Handshake
        String boardLine = in.readLine();   // BOARD w h
        String noteLine  = in.readLine();   // NOTE w h
        String colorLine = in.readLine();   // COLORS c1,c2,...

        String[] b = boardLine.split("\\s+");
        String[] n = noteLine.split("\\s+");

        int bw = Integer.parseInt(b[1]);
        int bh = Integer.parseInt(b[2]);
        int nw = Integer.parseInt(n[1]);
        int nh = Integer.parseInt(n[2]);

        String[] cs = colorLine.substring("COLORS".length()).trim().split(",");
        List<String> colors = new ArrayList<>();
        for (String c : cs) colors.add(c.trim());

        handshake = new Handshake(bw, bh, nw, nh, colors);
        return handshake;
    }

    public Response sendCommand(String cmd) throws IOException {
        out.println(cmd);

        String first = in.readLine();
        if (first == null) throw new IOException("Server closed");

        boolean ok = first.startsWith("OK");
        List<String> payload = new ArrayList<>();

        if (ok) {
            String[] parts = first.split("\\s+");
            if (parts.length == 2) {
                int n = Integer.parseInt(parts[1]);
                for (int i = 0; i < n; i++) {
                    payload.add(in.readLine());
                }
            }
        }

        return new Response(ok, first, payload);
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
    }
}
