package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import util.DatabaseConnection;
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
    public static GameRoom getRoomById(String roomId) {
        return rooms.get(roomId); // Trả về GameRoom tương ứng với Room ID
    }

    public static GameRoom createNewRoom(ClientHandler client) {
        String roomId = UUID.randomUUID().toString(); // Tạo ID phòng duy nhất
        GameRoom newRoom = new GameRoom(roomId, client, null);  // Tạo phòng mới với chỉ một player
        rooms.put(roomId, newRoom);
        return newRoom;
    }

    public static synchronized void findOpponent(ClientHandler client) {
        System.out.println("Searching for opponent for client: " + client);  // Log khi server bắt đầu tìm đối thủ

        // Nếu có client khác trong hàng đợi, ghép cặp ngay
        if (!waitingClients.isEmpty()) {
            // Lấy đối thủ từ hàng đợi
            ClientHandler opponent = waitingClients.poll();

            // Lấy tên người chơi của cả client và đối thủ
            String clientName = client.getName(); // Lấy tên người chơi 1
            String opponentName = opponent.getName(); // Lấy tên người chơi 2

            String roomId = UUID.randomUUID().toString(); // Tạo ID phòng duy nhất
            GameRoom room = new GameRoom(roomId, client, opponent); // Thêm tên người chơi vào phòng

            opponent.setOpponent(client);
            client.setOpponent(opponent);
            rooms.put(roomId, room);
            room.saveRoomToDatabase(); // Lưu phòng vào cơ sở dữ liệu

            // Thông báo cho cả hai client về phòng chơi và bắt đầu trò chơi
            client.setRoom(room);
            opponent.setRoom(room);

            // Gửi thông báo về trò chơi

            client.sendMessage("ROOM_ID " + roomId); // Gửi ID phòng cho client
            opponent.sendMessage("ROOM_ID " + roomId); // Gửi ID phòng cho đối thủ

            // Thông báo rằng đối thủ đã được tìm thấy
            client.sendMessage("OPPONENT_FOUND");
            opponent.sendMessage("OPPONENT_FOUND");
            client.sendMessage("OPPONENT_FOUND " + opponent.getName()); // Gửi tên đối thủ
            opponent.sendMessage("OPPONENT_FOUND " + client.getName()); // Gửi tên đối thủ

            // Bắt đầu trò chơi
            room.startGame();
            System.out.println("Game started in room: " + roomId);  // Log khi phòng được tạo và trò chơi bắt đầu
        } else {
            // Nếu không có đối thủ, thêm vào hàng đợi
            waitingClients.add(client);
            client.sendMessage("WAIT_FOR_OPPONENT");
            System.out.println("Client added to waiting list: " + client);
        }
    }
    private static void saveRoomToDatabase(GameRoom room) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO rooms (id, player1, player2, board_state, is_player1_turn, game_over) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, room.getId());
            stmt.setString(2, room.getPlayer1().getName());
            stmt.setString(3, room.getPlayer2().getName());
            stmt.setString(4, room.getBoardState());
            stmt.setBoolean(5, room.isPlayer1Turn());
            stmt.setBoolean(6, room.isGameOver());
            stmt.executeUpdate();
            System.out.println("Room saved to database: " + room.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Xử lý nước đi
    public static synchronized void handleMove(String roomId, String playerMove, ClientHandler sender) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            // Kiểm tra xem có phải lượt của người chơi không
            if ((sender == room.getPlayer1() && room.isPlayer1Turn()) ||
                    (sender == room.getPlayer2() && !room.isPlayer1Turn())) {

                // Xử lý nước đi và gửi cho đối thủ
                room.broadcastMove(playerMove, sender);  // Gửi nước đi cho đối thủ
                System.out.println("Move handled for room: " + roomId + " Move: " + playerMove);

                // Đổi lượt cho người chơi
                room.switchTurn();
            } else {
                // Gửi thông báo yêu cầu chờ lượt
                sender.sendMessage("WAIT_FOR_YOUR_TURN");
                System.out.println("Client tried to move out of turn: " + sender);
            }
        }
    }
}
