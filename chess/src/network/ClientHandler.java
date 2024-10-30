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
            synchronized (clientWriters) {
                clientWriters.add(out);
            }

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("FIND_OPPONENT")) {
                    // Kiểm tra xem có người chơi khác đã kết nối hay không
                    if (clientWriters.size() > 1) {
                        // Gửi tín hiệu cho cả hai người chơi rằng họ đã được ghép đôi
                        out.println("READY_TO_START");
                        // Bạn có thể tùy chỉnh gửi thông điệp cho đối thủ tại đây nếu cần
                    } else {
                        // Nếu không có đối thủ, thông báo cho client
                        out.println("WAIT_FOR_OPPONENT");
                    }
                }
                // Xử lý các thông điệp khác tại đây
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clientWriters) {
                clientWriters.remove(out);
            }
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
