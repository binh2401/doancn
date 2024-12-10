package network;

import java.io.*;
import java.net.*;

import AI.Board;
import giaodien.StartWindow;

import javax.swing.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private StartWindow startWindow;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String roomId;
    private Board board;
    private boolean isGameStarted;
    private Runnable onOpponentFound; // Khai báo biến onOpponentFound
    private String opponentName;
    public String getOpponentName() {
        return this.opponentName;
    }
    // Phương thức khởi tạo để bắt đầu kết nối
    public void start() {
        try {
            if (socket != null && !socket.isClosed()) {
                System.out.println("Existing socket is still open. Closing it...");
                closeSocket();
            }
            // Tạo kết nối tới server
            socket = new Socket(SERVER_ADDRESS, PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Mở cửa sổ StartWindow khi kết nối thành công
            SwingUtilities.invokeLater(() -> {
                startWindow = new StartWindow(this);
                startWindow.setVisible(true);
            });

            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + PORT);

            // Tạo luồng nhận dữ liệu từ server
            // Kiểm tra trạng thái socket sau khi kết nối
            if (!isSocketActive()) {
                System.err.println("Socket is not active after connecting. Exiting...");
                return;
            }

            // Tạo luồng nhận nước đi từ đối thủ
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received message: " + message);

                        // Kiểm tra trạng thái socket
                        if (!isSocketActive()) {
                            System.out.println("Socket is not active. Exiting...");
                            break;
                        }

                        // Kiểm tra trạng thái Output Stream
                        System.out.println("Output stream status: " + (out != null ? "Initialized" : "Null"));

                        String finalMessage = message;
                        SwingUtilities.invokeLater(() -> processMessage(finalMessage));
                    }} catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    closeSocket(); // Đảm bảo socket được đóng khi hoàn thành công việc
                }
            }).start();

        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến server. Vui lòng thử lại sau!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean isSocketActive() {
        boolean active = socket != null && !socket.isClosed() && socket.isConnected();
        System.out.println("Socket status - isClosed: " + (socket != null && socket.isClosed()) +
                ", isConnected: " + (socket != null && socket.isConnected()));
        return active;
    }
    private void closeSocket() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket closed successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    // Luồng nhận tin nhắn từ server
    private void receiveMessages() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                processMessage(message);
            }
        } catch (IOException e) {
            System.err.println("Connection to server lost: " + e.getMessage());
        } finally {
            cleanup(); // Đảm bảo tài nguyên được giải phóng
        }
    }

    // Xử lý tin nhắn từ server
    private void processMessage(String message) {
        System.out.println("Received message: " + message);

        if (message.startsWith("GAME_START")) {

            SwingUtilities.invokeLater(() -> startWindow.enablePlayButton()); // Kích hoạt nút
        } else if (message.startsWith("ROOM_ID")) {
            String[] parts = message.split(" ");
            if (parts.length > 1) {
                roomId = parts[1]; // Gán roomId từ phần thứ hai trong thông điệp
                 getRoomId(roomId);

                System.out.println("Room ID received: " + roomId);

                SwingUtilities.invokeLater(() -> {
                    if (startWindow != null) {
                        startWindow.setRoomId(roomId);  // Cập nhật roomId trong StartWindow
                    }
                });
            } else {
                System.err.println("Invalid ROOM_ID message format. No room ID found.");
            }
        }else
            if (message.startsWith("MOVE")) {
                System.out.println("message");
            updateBoard(message.substring(5)); // Cập nhật bàn cờ với nước đi
        } else if (message.startsWith("OPPONENT_FOUND")) {
                System.out.println("Opponent found, notifying client...");
            String[] parts = message.split(" "); // Phân tách thông điệp
            if (parts.length > 1) {
                this.opponentName = parts[1]; // Gán tên đối thủ
                System.out.println("Opponent found: " + opponentName);
                SwingUtilities.invokeLater(() -> {
                    if (startWindow != null) {
                        startWindow.notifyOpponentFound(); // Gọi hàm thông báo
                    }
                });
            }else if (message.startsWith("WAIT_FOR_YOUR_TURN")) {  // Xử lý thông điệp này
                System.out.println("You need to wait for your turn.");}

            else {
                System.err.println("Invalid OPPONENT_FOUND message format.");
            }
        }
    }

    // Gửi nước đi đến server
//    public void sendMove(String move) {
//        if (roomId != null && isGameStarted) {
//            System.out.println("Sending move to server: " + move);
//            sendMessage("MOVE " + roomId + " " + move); // Gửi nước đi cùng ID phòng
//        } else {
//            System.out.println("Game not started or room ID is null. Cannot send move.");
//        }
//    }

    // Cập nhật bàn cờ khi nhận được thông điệp từ server
    private void updateBoard(String move) {
        System.out.println("Updating board with move: " + move);
        SwingUtilities.invokeLater(() -> {
            startWindow.updateBoard(move); // Cập nhật bàn cờ trong StartWindow
        });
    }

    // Gửi thông điệp đến server
    public void sendMessage(String message, String roomId) {
        // Kiểm tra nếu output stream hoặc socket không còn hoạt động
        String a = (roomId != null && !roomId.isEmpty()) ? "ROOM_ID:" + roomId + "," + message : message;
        // Sau khi đảm bảo rằng output stream vẫn hoạt động, gửi tin nhắn
        if (out != null && isSocketActive()) {
            if(roomId==null){
            try {
                System.out.println("Sending message to server: " + a);
                out.println(message);
                out.flush();
            } catch (Exception e) {
                System.err.println("Error sending message to server: " + e.getMessage());
                e.printStackTrace();
            }}
            else {
                System.out.println("Sending message to server: " + a);
                out.println(a);
                out.flush();
            }
        } else {
            System.err.println("Out stream is null or socket is not active. Make sure the client is connected to the server.");
            if (socket != null) {
                System.out.println("Socket status - isClosed: " + socket.isClosed() + ", isConnected: " + socket.isConnected());
            }
        }
    }
    private void sendPing() {
        if (out != null && isSocketActive()) {
            System.out.println("Sending ping to server.");
            out.println("ping");
            out.flush();
        } else {
            System.err.println("Output stream is null or socket is not active.");
        }
    }
    // Tìm đối thủ
    public void findOpponent() {
        System.out.println("Requesting to find an opponent...");
        sendMessage("FIND_OPPONENT", null);
    }

    // Tạo phòng đấu
    public void createRoom() {
        System.out.println("Creating a new room...");
        sendMessage("CREATE_ROOM",null);
    }

    // Đặt callback khi tìm được đối thủ
    public void setOnOpponentFound(Runnable onOpponentFound) {
        this.onOpponentFound = onOpponentFound; // Gán Runnable vào biến
    }

    // Thông báo khi tìm được đối thủ
    public void notifyOpponentFound() {
        if (onOpponentFound != null) {
            onOpponentFound.run(); // Thực thi callback khi đối thủ được tìm thấy
        }
    }

    // Dọn dẹp tài nguyên
    private void cleanup() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Resources cleaned up and connection closed.");
        } catch (IOException e) {
            System.err.println("Error while closing resources: " + e.getMessage());
        }
    }
    public String getRoomId(String roomid) {
        if (roomid == null) {
            System.out.println("Room ID is not set yet.");
        } else {
            System.out.println("Room ID is: " + roomid);
        }
        return roomid;
    }
    // Phương thức main để chạy Client
    public static void main(String[] args) {
        Client client = new Client();  // Tạo đối tượng Client
        client.start();  // Bắt đầu kết nối và khởi tạo mọi thứ
    }

}
