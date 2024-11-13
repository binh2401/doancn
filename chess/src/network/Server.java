package network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final Map<String, GameRoom> rooms = new HashMap<>(); // Quản lý các phòng chơi
    private static final Queue<ClientHandler> waitingClients = new LinkedList<>(); // Hàng đợi tìm đối thủ

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");

            while (true) {
                // Chấp nhận kết nối từ client
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Tạo ClientHandler cho mỗi client kết nối và chạy trong một luồng riêng biệt
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tìm đối thủ và tạo phòng chơi
    public static synchronized void findOpponent(ClientHandler client) {
        System.out.println("Searching for opponent for client: " + client);  // Log khi server bắt đầu tìm đối thủ

        // Nếu có client khác trong hàng đợi, ghép cặp ngay
        if (!waitingClients.isEmpty()) {
            // Lấy đối thủ từ hàng đợi
            ClientHandler opponent = waitingClients.poll();
            String roomId = UUID.randomUUID().toString(); // Tạo ID phòng duy nhất
            GameRoom room = new GameRoom(roomId, client, opponent);
            rooms.put(roomId, room);

            // Thông báo cho cả hai client về phòng chơi và bắt đầu trò chơi
            client.setRoom(room);
            opponent.setRoom(room);
            client.sendMessage("GAME_START " + roomId);
            opponent.sendMessage("GAME_START " + roomId);
            System.out.println("Game started in room: " + roomId);  // Log khi phòng được tạo và trò chơi bắt đầu
        } else {
            // Nếu không có đối thủ, thêm vào hàng đợi
            waitingClients.add(client);
            client.sendMessage("WAIT_FOR_OPPONENT");
            System.out.println("Client added to waiting list: " + client);
        }
    }

    // Xử lý nước đi
    public static synchronized void handleMove(String roomId, String move, ClientHandler sender) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            // Kiểm tra xem có phải lượt của người chơi không
            if ((sender == room.getPlayer1() && room.isPlayer1Turn()) ||
                    (sender == room.getPlayer2() && !room.isPlayer1Turn())) {

                // Xử lý nước đi và gửi cho đối thủ
                room.broadcastMove(move, sender);  // Gửi nước đi cho đối thủ

                // Đổi lượt cho người chơi
                room.switchTurn();
            } else {
                // Gửi thông báo yêu cầu chờ lượt
                sender.sendMessage("WAIT_FOR_YOUR_TURN");
            }
        }
    }
}
