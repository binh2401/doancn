package network;

import java.io.*;
import java.net.*;
import auth.StartWindow;
import dao.UserDAO;
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
    // Phương thức khởi tạo để bắt đầu kết nối
    public void start() {
        try {
            // Tạo kết nối tới server
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Mở cửa sổ StartWindow khi kết nối thành công
            SwingUtilities.invokeLater(() -> {
                startWindow = new StartWindow(this);
                startWindow.setVisible(true);
            });

            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + PORT);

            // Tạo luồng nhận nước đi từ đối thủ
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        processMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();  // Đảm bảo rằng socket chỉ đóng khi hoàn thành công việc
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
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
        }
    }

    // Gửi nước đi đến server
    public void sendMove(String move) {
        if (roomId != null && isGameStarted) {
            System.out.println("Sending move to server: " + move);
            out.println("MOVE " + roomId + " " + move); // Gửi nước đi cùng ID phòng
            out.flush();
        } else {
            System.out.println("Game not started or room ID is null. Cannot send move.");
        }
    }

    // Cập nhật bàn cờ khi nhận được thông điệp từ server
    private void updateBoard(String move) {
        System.out.println("Updating board with move: " + move);
        SwingUtilities.invokeLater(() -> {
            startWindow.updateBoard(move); // Cập nhật bàn cờ trong StartWindow
        });
    }

    // Gửi thông điệp đến server
    public void sendMessage(String message) {
        if (out != null) {
            System.out.println("Sending message to server: " + message);
            out.println(message);
            out.flush();
        } else {
            System.out.println("Out stream is null, cannot send message.");
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
}
