package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameRoom room;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("FIND_OPPONENT")) {
                    Server.findOpponent(this); // Tìm đối thủ
                } else if (message.startsWith("MOVE")) {
                    System.out.println(message);
                    if (room != null) {
                        room.broadcastMove(message, this); // Chuyển nước đi cho đối thủ
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
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    private void handleMove(String message) {
        if (room != null) {
            String move = message.substring(5); // Bỏ "MOVE "
            room.broadcastMove(move, this);    // Gửi nước đi tới đối thủ
        }
    }
    public void setRoom(GameRoom room) {
        this.room = room;
    }
}

