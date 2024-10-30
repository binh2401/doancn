import network.Server;

import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private Client client; // Khách hàng để kết nối với máy chủ (nếu có)
    private StartWindow startWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main(); // Khởi tạo Main
            main.createAndShowGUI(); // Gọi phương thức để hiển thị GUI
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Đồ án cờ tướng AI");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false); // Ẩn frame chính ban

        startWindow = new StartWindow(this,this.client); // Truyền Main và Client
        startWindow.setVisible(true); // Hiển thị StartWindow
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
