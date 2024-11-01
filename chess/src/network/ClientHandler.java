package network;

import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandler implements Runnable{
    private Socket socket;
    private Server server;
    private PrintWriter out;
    private ClientHandler opponent;
    private BufferedReader in;
    private static List<PrintWriter> clientWriters = new ArrayList<>();
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("FIND_OPPONENT")) {
                    server.findOpponent(this); // Tìm đối thủ thông qua server
                } else if (message.startsWith("MOVE")) {
                    if (opponent != null) {
                        opponent.sendMessage(message); // Chuyển nước đi cho đối thủ
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
          //  server.removeClient(this); // Xóa client khỏi server khi ngắt kết nối
        }
    }


    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void setOpponent(ClientHandler opponent) {
        this.opponent = opponent;
    }

    public ClientHandler getOpponent() {
        return opponent;
    }
}
