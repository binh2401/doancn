import network.Server;

import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame; // Để lưu trữ JFrame chính
    private Client client; // Khách hàng để kết nối với máy chủ (nếu có)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Đồ án cờ tướng AI");
        frame.setLayout(new BorderLayout()); // Sử dụng BorderLayout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false); // Ẩn frame chính ban đầu
        StartWindow startWindow = new StartWindow(this, client); // Truyền Main và Client
        startWindow.setVisible(true); // Hiển thị StartWindow
    }

    // Constructor không nhận tham số
    public Main() {
        // Khởi tạo các thành phần cần thiết khác nếu có
    }

    // Constructor nhận Client làm tham số
    public Main(Client client) {
        this.client = client;
        // Khởi tạo các thành phần cần thiết khác nếu có
    }

    public void startGame() {
        // Kiểm tra xem frame có được khởi tạo không
        if (frame == null) {
            frame = new JFrame("Đồ án cờ tướng AI"); // Khởi tạo frame nếu chưa có
            frame.setLayout(new BorderLayout()); // Sử dụng BorderLayout
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame.getContentPane().removeAll(); // Xóa cửa sổ hiện tại
        }

        // Tạo đối tượng Board để vẽ bàn cờ
        Board board = new Board();

        // Tạo đối tượng FunctionPanel
        FunctionPanel functionPanel = new FunctionPanel(board); // Tạo FunctionPanel

        // Thêm đối tượng Board và FunctionPanel vào JFrame
        frame.add(board, BorderLayout.CENTER); // Bàn cờ ở giữa
        frame.add(functionPanel, BorderLayout.EAST); // Bảng chức năng ở bên phải

        // Sử dụng pack() để tự động điều chỉnh kích thước cửa sổ phù hợp
        frame.pack();
        frame.setVisible(true); // Hiện cửa sổ chính
    }
}
