package network;

import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandler implements Runnable{
    private Socket socket;
    private Server server;
    private PrintWriter out;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("MOVE")) {
                    server.broadcastMove(message, this); // Gửi nước đi tới client đối thủ
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
