// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ProtocolClient {
   private Socket socket;
   private BufferedReader in;
   private PrintWriter out;
   private Handshake handshake;

   public ProtocolClient() {
   }

   public boolean isConnected() {
      return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
   }

   public Handshake connect(String var1, int var2) throws IOException {
      this.socket = new Socket(var1, var2);
      this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      this.out = new PrintWriter(this.socket.getOutputStream(), true);
      String var3 = this.in.readLine();
      String var4 = this.in.readLine();
      String var5 = this.in.readLine();
      String[] var6 = var3.split("\\s+");
      String[] var7 = var4.split("\\s+");
      int var8 = Integer.parseInt(var6[1]);
      int var9 = Integer.parseInt(var6[2]);
      int var10 = Integer.parseInt(var7[1]);
      int var11 = Integer.parseInt(var7[2]);
      String[] var12 = var5.substring("COLORS".length()).trim().split(",");
      ArrayList var13 = new ArrayList();
      String[] var14 = var12;
      int var15 = var12.length;

      for(int var16 = 0; var16 < var15; ++var16) {
         String var17 = var14[var16];
         var13.add(var17.trim());
      }

      this.handshake = new Handshake(var8, var9, var10, var11, var13);
      return this.handshake;
   }

   public Response sendCommand(String var1) throws IOException {
      this.out.println(var1);
      String var2 = this.in.readLine();
      if (var2 == null) {
         throw new IOException("Server closed");
      } else {
         boolean var3 = var2.startsWith("OK");
         ArrayList var4 = new ArrayList();
         if (var3) {
            String[] var5 = var2.split("\\s+");
            if (var5.length == 2) {
               int var6 = Integer.parseInt(var5[1]);

               for(int var7 = 0; var7 < var6; ++var7) {
                  var4.add(this.in.readLine());
               }
            }
         }

         return new Response(var3, var2, var4);
      }
   }

   public void disconnect() throws IOException {
      if (this.socket != null) {
         this.socket.close();
      }

   }

   public static class Handshake {
      public final int boardW;
      public final int boardH;
      public final int noteW;
      public final int noteH;
      public final List<String> colors;

      public Handshake(int var1, int var2, int var3, int var4, List<String> var5) {
         this.boardW = var1;
         this.boardH = var2;
         this.noteW = var3;
         this.noteH = var4;
         this.colors = var5;
      }
   }

   public static class Response {
      public final boolean ok;
      public final String firstLine;
      public final List<String> payload;

      public Response(boolean var1, String var2, List<String> var3) {
         this.ok = var1;
         this.firstLine = var2;
         this.payload = var3;
      }
   }
}
