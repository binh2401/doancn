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
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tìm đối thủ và tạo phòng chơi
    public static synchronized void findOpponent(ClientHandler client) {
        if (waitingClients.isEmpty()) {
            waitingClients.add(client); // Thêm client vào hàng đợi
            client.sendMessage("WAIT_FOR_OPPONENT");
        } else {
            ClientHandler opponent = waitingClients.poll(); // Ghép cặp với đối thủ
            String roomId = UUID.randomUUID().toString(); // Tạo ID phòng duy nhất
            GameRoom room = new GameRoom(roomId, client, opponent);
            rooms.put(roomId, room);

            // Thông báo cho cả hai client
            client.setRoom(room);
            opponent.setRoom(room);
            client.sendMessage("GAME_START " + roomId);
            opponent.sendMessage("GAME_START " + roomId);
        }
    }

    // Xử lý nước đi
    public static synchronized void handleMove(String roomId, String move, ClientHandler sender) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            room.broadcastMove(move, sender); // Gửi nước đi cho đối thủ
        }
    }
}




