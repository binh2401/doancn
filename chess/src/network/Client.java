package network;

import java.io.*;
import java.net.*;
import auth.StartWindow;

import javax.swing.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private StartWindow startWindow;
    private PrintWriter out;
    private Runnable onOpponentFound;
    private String roomId;

    // Phương thức khởi tạo để bắt đầu kết nối
    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            this.out = out;

            // Mở cửa sổ auth.StartWindow khi kết nối thành công
            SwingUtilities.invokeLater(() -> {
                startWindow = new StartWindow(this);
                startWindow.setVisible(true);
            });

            // Tạo luồng nhận nước đi từ đối thủ
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("GAME_START")) {
                            roomId = message.split(" ")[1]; // Lưu ID phòng
                            System.out.println("Game started in room: " + roomId);
                            SwingUtilities.invokeLater(() -> startWindow.enablePlayButton()); // Kích hoạt nút
                        } else if (message.startsWith("MOVE")) {
                            updateBoard(message.substring(5));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Gửi nước đi từ console
            String userInput;
            while ((userInput = console.readLine()) != null) {
                sendMove(userInput); // Gửi nước đi tới server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove(String move) {
        if (roomId != null) {
            out.println("MOVE " + roomId + " " + move); // Gửi nước đi cùng ID phòng
            out.flush();
        }
    }

    private void updateBoard(String move) {
        SwingUtilities.invokeLater(() -> {
            startWindow.updateBoard(move); // Cập nhật bàn cờ trong auth.StartWindow
        });
    }

    public void sendMessage(String message) {
        if (out != null) {
            System.out.println("Sent message to client: " + message);
            out.println(message);
        } else {
            System.out.println("Out stream is null, cannot send message.");
        }
    }

    public void findOpponent() {
        sendMessage("FIND_OPPONENT");
    }

    public void setOnOpponentFound(Runnable onOpponentFound) {
        this.onOpponentFound = onOpponentFound;
    }

    public void notifyOpponentFound() {
        if (onOpponentFound != null) {
            onOpponentFound.run();
        }
    }

    // Phương thức main để chạy Client
    public static void main(String[] args) {
        Client client = new Client();  // Tạo đối tượng Client
        client.start();  // Bắt đầu kết nối và khởi tạo mọi thứ
    }
}
