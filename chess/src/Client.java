import javax.swing.*;
import java.io.*;
import java.net.*;
public class Client {
    private static final String SERVER_ADDRESS = "localhost"; // Địa chỉ server
    private static final int PORT = 12345;
    private StartWindow startWindow; // Tham chiếu đến cửa sổ StartWindow

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            // Mở cửa sổ StartWindow khi kết nối thành công
            SwingUtilities.invokeLater(() -> {
                startWindow = new StartWindow(new Main()); // Truyền Main vào StartWindow
                startWindow.setVisible(true); // Hiển thị StartWindow
            });

            // Tạo luồng nhận nước đi từ đối thủ
            new Thread(() -> {
                String opponentMove;
                try {
                    while ((opponentMove = in.readLine()) != null) {
                        System.out.println("Opponent move: " + opponentMove);
                        updateBoard(opponentMove); // Cập nhật bàn cờ với nước đi của đối thủ
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Gửi nước đi từ console
            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput); // Gửi nước đi đến server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức cập nhật bàn cờ
    private void updateBoard(String move) {
        // Ở đây bạn cần viết mã để cập nhật bàn cờ trong giao diện người dùng của bạn
        // Giả sử bạn có một phương thức trong StartWindow hoặc Main để cập nhật
        // Ví dụ:
        startWindow.updateBoard(move); // Gọi phương thức updateBoard trong StartWindow
    }
}
