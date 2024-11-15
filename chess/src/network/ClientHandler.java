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
                } else if (message.equals("CREATE_ROOM")) {
                    System.out.println("Creating room for client: " + socket.getInetAddress());  // Log khi client yêu cầu tạo phòng
                    createRoom();  // Tạo phòng mới
                } else if (message.startsWith("MOVE")) {
                    System.out.println("Move received: " + message);  // Log nước đi nhận được từ client
                    handleMove(message);  // Xử lý nước đi
                } else if (message.equals("EXIT")) {
                    System.out.println("Client exiting: " + socket.getInetAddress());  // Log khi client thoát
                    closeConnection();
                    break;  // Thoát khỏi vòng lặp khi client ngắt kết nối
                } else {
                    // Nếu thông điệp không hợp lệ, gửi thông báo lỗi
                    sendMessage("INVALID_COMMAND");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Nếu có lỗi trong quá trình đọc dữ liệu từ client, đóng kết nối
            closeConnection();
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

    // Tạo phòng mới cho client
    private void createRoom() {
        GameRoom newRoom = Server.createNewRoom(this);  // Tạo một phòng mới và gán cho client
        setRoom(newRoom);  // Gán phòng mới cho client
        sendMessage("ROOM_CREATED " + newRoom.getId());  // Thông báo cho client rằng phòng đã được tạo

        System.out.println("Room created with ID: " + newRoom.getId());  // Log thông tin phòng được tạo

        // Đợi đối thủ gia nhập phòng
        Server.findOpponent(this);  // Tìm đối thủ cho client trong phòng
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
            sendMessage("WAIT_FOR_YOUR_TURN");
        }
    }

    // Gửi thông báo khi tìm được đối thủ và bắt đầu trò chơi
    public void notifyOpponentFound(ClientHandler opponent) {
        sendMessage("OPPONENT_FOUND " + opponent.socket.getInetAddress());  // Thông báo đối thủ cho client
        opponent.sendMessage("OPPONENT_FOUND " + this.socket.getInetAddress());  // Thông báo đối thủ cho client kia
        startGame();  // Bắt đầu trò chơi
    }

    // Bắt đầu trò chơi
    private void startGame() {
        sendMessage("GAME_START " + room.getId());  // Gửi thông báo bắt đầu trò chơi cho client
        System.out.println("Game started in room: " + room.getId());  // Log khi trò chơi bắt đầu
    }

    // Đóng kết nối của client
    private void closeConnection() {
        try {
            // Kiểm tra và đóng phòng khi client thoát
            if (room != null) {
                room.removePlayer(this);  // Xóa người chơi khỏi phòng
            }
            socket.close();
            System.out.println("Connection closed for client: " + socket.getInetAddress());  // Log khi kết nối bị đóng
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Thông báo khi trò chơi kết thúc
    public void endGame(String result) {
        sendMessage("GAME_OVER " + result);  // Gửi thông báo kết thúc trò chơi cho client
        if (room != null) {
            room.broadcastMessage("GAME_OVER " + result);  // Gửi thông báo kết thúc trò chơi cho đối thủ
        }
        closeConnection();  // Đóng kết nối sau khi kết thúc trò chơi
    }
}
