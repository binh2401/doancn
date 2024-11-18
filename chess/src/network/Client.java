package network;

import java.io.*;
import java.net.*;
import auth.StartWindow;

import model.User;

import javax.swing.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private StartWindow startWindow;
    private PrintWriter out;
    private BufferedReader in;
    private String roomId;
    private boolean isGameStarted;
    private Runnable onOpponentFound; // Khai báo biến onOpponentFound
    private String username;
    private String password;
    private Socket socket; // Lưu trữ socket

    // Phương thức khởi tạo để bắt đầu kết nối
    public void start() {
        try {
            // Kiểm tra và đóng socket cũ nếu cần
            if (socket != null && !socket.isClosed()) {
                System.out.println("Existing socket is still open. Closing it...");
                closeSocket();
            }

            // Tạo kết nối tới server
            socket = new Socket(SERVER_ADDRESS, PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Output stream (out) initialized successfully.");
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Mở cửa sổ StartWindow khi kết nối thành công
            SwingUtilities.invokeLater(() -> {
                startWindow = new StartWindow(this);
                startWindow.setVisible(true);
            });

            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + PORT);

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

                        processMessage(message);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    closeSocket(); // Đảm bảo socket được đóng khi hoàn thành công việc
                }
            }).start();

        } catch (IOException e) {
            System.err.println("Failed to connect to server at " + SERVER_ADDRESS + ":" + PORT);
            e.printStackTrace();
        }
    }


    // Kiểm tra trạng thái socket
    private boolean isSocketActive() {
        boolean active = socket != null && !socket.isClosed() && socket.isConnected();
        System.out.println("Socket status - isClosed: " + (socket != null && socket.isClosed()) +
                ", isConnected: " + (socket != null && socket.isConnected()));
        return active;
    }

    // Đóng socket một cách an toàn
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

    // Xử lý tin nhắn từ server
    private void processMessage(String message) {
        System.out.println("Received message: " + message);

        if (message.startsWith("GAME_START")) {
            roomId = message.split(" ")[1]; // Lưu ID phòng
            System.out.println("Game started in room: " + roomId);
            SwingUtilities.invokeLater(() -> startWindow.enablePlayButton()); // Kích hoạt nút
        } else if (message.startsWith("MOVE")) {
            updateBoard(message.substring(5)); // Cập nhật bàn cờ với nước đi
        } else if (message.startsWith("OPPONENT_FOUND")) {
            System.out.println("Opponent found, notifying client...");
            SwingUtilities.invokeLater(() -> {
                if (startWindow != null) {
                    startWindow.notifyOpponentFound(); // Thông báo tìm thấy đối thủ
                }
            });
        } else if (message.startsWith("WAIT_FOR_YOUR_TURN")) {  // Xử lý thông điệp này
            System.out.println("You need to wait for your turn.");
        }
    }

    // Gửi nước đi đến server
    public void sendMove(String move) {
        if (roomId != null && isGameStarted && isSocketActive()) {
            System.out.println("Sending move to server: " + move);
            out.println("MOVE " + roomId + " " + move); // Gửi nước đi cùng ID phòng
            out.flush();
        } else {
            System.out.println("Game not started, room ID is null, or socket is not active. Cannot send move.");
        }
    }

    // Gửi thông điệp đến server
    // Gửi thông điệp đến server
    public void sendMessage(String message) {
        // Kiểm tra nếu output stream hoặc socket không còn hoạt động, tái kết nối hoặc thông báo lỗi
        if (out == null || !isSocketActive()) {
            System.err.println("Output stream is null or socket is not active. Reinitializing connection...");

            try {
                // Kiểm tra trạng thái socket
                if (socket == null || socket.isClosed()) {
                    System.out.println("Socket is closed, attempting to reconnect...");
                    // Không gọi lại startWindow.setVisible(true) để tránh khởi tạo lại cửa sổ
                    startConnection();  // Gọi phương thức khởi tạo kết nối mới mà không tạo lại cửa sổ
                } else {
                    // Nếu socket vẫn mở, chỉ cần khởi tạo lại output stream
                    out = new PrintWriter(socket.getOutputStream(), true);
                    System.out.println("Output stream reinitialized.");
                }
            } catch (IOException e) {
                System.err.println("Failed to reinitialize output stream: " + e.getMessage());
                e.printStackTrace();
                return;  // Nếu không tái kết nối được, dừng việc gửi tin nhắn
            }
        }

        // Sau khi đảm bảo rằng output stream vẫn hoạt động, gửi tin nhắn
        if (out != null && isSocketActive()) {
            try {
                System.out.println("Sending message to server: " + message);
                out.println(message);  // Gửi tin nhắn
                out.flush();
            } catch (Exception e) {
                System.err.println("Error sending message to server: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Out stream is null or socket is not active. Make sure the client is connected to the server.");
            if (socket != null) {
                System.out.println("Socket status - isClosed: " + socket.isClosed() + ", isConnected: " + socket.isConnected());
            }
        }
    }
    private void startConnection() {
        try {
            // Đảm bảo không khởi tạo lại StartWindow khi tái kết nối
            if (socket != null && !socket.isClosed()) {
                System.out.println("Closing existing socket before reconnecting...");
                closeSocket();  // Đóng socket cũ nếu có
            }

            // Tạo kết nối mới tới server
            socket = new Socket(SERVER_ADDRESS, PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Reconnected to server at " + SERVER_ADDRESS + ":" + PORT);

            // Tạo luồng nhận nước đi từ đối thủ
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received message: " + message);
                        processMessage(message);  // Xử lý tin nhắn
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    closeSocket(); // Đảm bảo socket được đóng khi hoàn thành công việc
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Failed to reconnect to server at " + SERVER_ADDRESS + ":" + PORT);
            e.printStackTrace();
        }
    }

    // Tìm đối thủ
    public void findOpponent() {
        System.out.println("Requesting to find an opponent...");
        sendMessage("FIND_OPPONENT");
    }

    // Tạo phòng đấu
    public void createRoom() {
        System.out.println("Creating a new room...");
        sendMessage("CREATE_ROOM");
    }

    // Đặt callback khi tìm được đối thủ
    public void setOnOpponentFound(Runnable onOpponentFound) {
        this.onOpponentFound = onOpponentFound; // Gán Runnable vào biến
    }

    // Phương thức main để chạy Client
    public static void main(String[] args) {
        Client client = new Client();  // Tạo đối tượng Client
        client.start();  // Bắt đầu kết nối và khởi tạo mọi thứ
    }

    // Thông báo khi tìm được đối thủ
    public void notifyOpponentFound() {
        if (onOpponentFound != null) {
            onOpponentFound.run(); // Thực thi callback khi đối thủ được tìm thấy
        }
    }

    private void updateBoard(String move) {
        System.out.println("Updating board with move: " + move);
        if (startWindow != null) {
            try {
                SwingUtilities.invokeLater(() -> {
                    startWindow.updateBoard(move); // Cập nhật bàn cờ trong StartWindow
                });
            } catch (Exception e) {
                System.err.println("Error updating board: " + e.getMessage());
                e.printStackTrace(); // In ra chi tiết lỗi
            }
        } else {
            System.err.println("Error: startWindow is null.");
        }
    }

}
