import javax.swing.*;
import java.io.*;
import java.net.*;
public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private StartWindow startWindow;
    private PrintWriter out; // Khai báo biến out
    private Runnable onOpponentFound;
    private Main main;



    public static void main(String[] args) {
        Client client = new Client();

        client.start();
    }



    private void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            // Gán giá trị cho biến out
            this.out = out;

            // Mở cửa sổ StartWindow khi kết nối thành công
            SwingUtilities.invokeLater(() -> {
                this.main= new Main();
                startWindow = new StartWindow(this.main, this); // Truyền Clientnew  vào Main
                startWindow.setVisible(true); // Hiển thị StartWindow
            });

            // Tạo luồng nhận nước đi từ đối thủ
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        if (message.equals("READY_TO_START")) {
                            SwingUtilities.invokeLater(() -> {
                                startWindow.enablePlayButton(); // Kích hoạt nút
                                if (this.onOpponentFound != null) {
                                    this.onOpponentFound.run(); // Gọi phương thức onOpponentFound
                                }
                            });
                        } else {
                            System.out.println("Opponent move: " + message);
                            updateBoard(message); // Cập nhật bàn cờ với nước đi của đối thủ
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Lỗi khi đọc luồng từ đối thủ hoặc luồng bị đóng.");
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
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    public void findOpponent() {
        // Gửi yêu cầu tìm đối thủ đến server
        sendMessage("FIND_OPPONENT");
    }

    public void setOnOpponentFound(Runnable onOpponentFound) {
        this.onOpponentFound = onOpponentFound;
    }

    // Gọi phương thức này khi nhận được thông báo từ server về việc tìm thấy đối thủ
    public void notifyOpponentFound() {
        if (onOpponentFound != null) {
            onOpponentFound.run();
        }
    }
    // Phương thức gửi nước đi tới server
    public void sendMove(String move) {
        out.println("MOVE " + move); // Gửi nước đi tới server
    }

    private void updateBoard(String move) {
        startWindow.updateBoard(move); // Cập nhật bàn cờ trong StartWindow
    }
}
