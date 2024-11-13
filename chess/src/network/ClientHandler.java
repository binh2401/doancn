package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
            // Khởi tạo các luồng vào ra
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received message from client: " + message);  // Log message nhận từ client

                // Phân loại thông điệp và xử lý tương ứng
                if (message.equals("FIND_OPPONENT")) {
                    System.out.println("Finding opponent for client: " + socket.getInetAddress());  // Log khi client yêu cầu tìm đối thủ
                    Server.findOpponent(this); // Tìm đối thủ
                } else if (message.startsWith("MOVE")) {
                    System.out.println("Move received: " + message);  // Log nước đi nhận được từ client
                    handleMove(message);  // Xử lý nước đi
                } else {
                    // Nếu thông điệp không hợp lệ, gửi thông báo lỗi
                    sendMessage("INVALID_COMMAND");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("Client disconnected: " + socket.getInetAddress());  // Log khi client ngắt kết nối
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức gửi thông điệp cho client
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            System.out.println("Sent message to client: " + message);  // Log thông báo gửi đến client
        }
    }

    // Gán phòng cho client
    public void setRoom(GameRoom room) {
        this.room = room;
        System.out.println("Client assigned to room: " + room.getId());  // Log khi client được gán phòng
    }

    // Trả về phòng mà client tham gia
    public GameRoom getRoom() {
        return room;
    }

    // Kiểm tra xem client có phải là player 1 trong phòng không
    public boolean isPlayer1() {
        return this == room.getPlayer1();
    }

    // Xử lý nước đi từ client
    private void handleMove(String message) {
        if (room != null && room.isPlayer1Turn() == (this == room.getPlayer1())) {
            String move = message.substring(5);  // Lấy nước đi sau từ khóa "MOVE "

            try {
                // Gửi nước đi tới đối thủ và đổi lượt cho người chơi
                room.broadcastMove(move, this);
                room.switchTurn();  // Đổi lượt cho người chơi
                System.out.println("Move handled: " + move);  // Log khi nước đi được xử lý
            } catch (Exception e) {
                e.printStackTrace();  // In lỗi nếu có
            }
        } else {
            // Nếu không phải lượt của client, gửi thông báo lỗi
            sendMessage("NOT_YOUR_TURN");
        }
    }
}
