import AI.Board;
import giaodien.FunctionPanel;
import giaodien.StartWindow;

import network.Client;

import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private Client client; // Khách hàng để kết nối với máy chủ (nếu có)
    private StartWindow startWindow;

    public static void main(String[] args) {


        // Khởi chạy ứng dụng bằng cách gọi phương thức khởi tạo GUI
        SwingUtilities.invokeLater(() -> {
            Main main = new Main(); // Khởi tạo Main
            main.createAndShowGUI(); // Gọi phương thức để hiển thị GUI
        });
    }

    public Main() {
        client = new Client(); // Khởi tạo client
        // Đảm bảo truyền client vào StartWindow
        startWindow = new StartWindow(client); // Truyền đối tượng client vào constructor của StartWindow
    }

    private void createAndShowGUI() {
        frame = new JFrame("Đồ án cờ tướng AI");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false); // Ẩn frame chính ban đầu

        // Hiển thị cửa sổ StartWindow
        startWindow.setVisible(true); // Hiển thị StartWindow
    }

//    public void startGameForPlayer(String difficulty) {
//
//    }

//    public void startGameForAI() {
//        if (frame == null) {
//            frame = new JFrame("Đồ án cờ tướng AI"); // Khởi tạo frame nếu chưa có
//            frame.setLayout(new BorderLayout());
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        } else {
//            frame.getContentPane().removeAll(); // Xóa cửa sổ hiện tại
//        }
//
//        // Hiển thị hộp thoại chọn độ khó AI
//        String[] options = {"Dễ", "Trung bình", "Khó"};
//        int choice = JOptionPane.showOptionDialog(null,
//                "Chọn độ khó của AI:",
//                "Chọn độ khó",
//                JOptionPane.DEFAULT_OPTION,
//                JOptionPane.INFORMATION_MESSAGE,
//                null,
//                options,
//                options[1]); // Mặc định chọn Trung bình
//
//        String difficulty = "medium"; // Mặc định là "Trung bình"
//        switch (choice) {
//            case 0:
//                difficulty = "easy"; // Người chơi chọn Dễ
//                break;
//            case 1:
//                difficulty = "medium"; // Người chơi chọn Trung bình
//                break;
//            case 2:
//                difficulty = "hard"; // Người chơi chọn Khó
//                break;
//            default:
//                System.out.println("Người chơi đã hủy chọn độ khó.");
//                return; // Nếu người chơi hủy chọn, kết thúc phương thức
//        }
//        Client client = new Client();
//        // Tạo đối tượng Board với độ khó đã chọn
//        Board board = new Board(true, difficulty,client); // Truyền thông tin độ khó vào Board
//        FunctionPanel functionPanel = new FunctionPanel(board); // Tạo AI.FunctionPanel
//
//        // Thêm các thành phần vào JFrame
//        frame.add(board, BorderLayout.CENTER); // Bàn cờ ở giữa
//        frame.add(functionPanel, BorderLayout.EAST); // Bảng chức năng bên phải
//
//        // Tự động điều chỉnh kích thước cửa sổ
//        frame.pack();
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Hiển thị cửa sổ toàn màn hình
//        frame.setVisible(true); // Hiển thị cửa sổ chính
//    }
}
