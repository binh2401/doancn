package AI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FunctionPanel extends JPanel {
    private Board board; // Biến để lưu tham chiếu đến Board

    public FunctionPanel(Board board) {
        this.board = board; // Lưu tham chiếu đến Board

        setLayout(new BorderLayout()); // Sử dụng BorderLayout

        // Panel để chứa các nút chức năng và nhãn thông báo
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Sắp xếp theo chiều dọc

        // Tạo các nút chức năng với kích thước cố định
        JButton resetButton = new JButton("Reset Game");
        JButton surrenderButton = new JButton("Surrender");
        JButton drawButton = new JButton("Draw");
        JButton backButton = new JButton("Roll Back");
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");

        // Đặt kích thước cố định cho các nút
        Dimension buttonSize = new Dimension(120, 40);
        resetButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        surrenderButton.setPreferredSize(buttonSize);
        drawButton.setPreferredSize(buttonSize);
        saveButton.setPreferredSize(buttonSize);
        loadButton.setPreferredSize(buttonSize);

        // Đảm bảo nút không bị giãn
        resetButton.setMaximumSize(buttonSize);
        surrenderButton.setMaximumSize(buttonSize);
        drawButton.setMaximumSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        saveButton.setPreferredSize(buttonSize);
        loadButton.setPreferredSize(buttonSize);

        // Đặt căn chỉnh cho các nút (giữa theo chiều ngang)
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        surrenderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm các nút vào buttonPanel
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(surrenderButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(drawButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(loadButton);

        // Tạo nhãn thông báo với kích thước cố định
        JLabel notificationLabel = new JLabel("Thông báo sẽ hiển thị ở đây", JLabel.CENTER);
        notificationLabel.setPreferredSize(new Dimension(200, 30)); // Đặt kích thước cố định
        notificationLabel.setMaximumSize(new Dimension(200, 30));
        notificationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm nhãn thông báo vào ngay dưới các nút
        buttonPanel.add(Box.createVerticalStrut(20)); // Khoảng cách giữa nút cuối và thông báo
        buttonPanel.add(notificationLabel);

        // Thêm bàn cờ vào giữa và buttonPanel vào bên phải
        add(board, BorderLayout.CENTER); // Đảm bảo bàn cờ nằm giữa
        add(buttonPanel, BorderLayout.EAST); // Đảm bảo các nút và thông báo nằm cạnh phải

        // Sự kiện cho nút Roll Back
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (board.undoLastMovePair()) {
                    notificationLabel.setText("Quân cờ đã quay lại nước đi trước đó.");
                } else {
                    notificationLabel.setText("Không thể quay lại nước đi.");
                }
            }
        });

        // Sự kiện cho nút Reset
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.resetGame();
                notificationLabel.setText("Trò chơi đã được khởi động lại.");
            }
        });

        // Sự kiện cho nút Surrender (Đầu hàng)
        surrenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.surrender(); // Giả sử có phương thức surrender trong Board
                notificationLabel.setText("Bạn đã đầu hàng.");
            }
        });

        // Sự kiện cho nút Draw (Hòa)
//        drawButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                board.declareDraw(); // Giả sử có phương thức declareDraw trong Board
//                notificationLabel.setText("Trận đấu đã hòa.");
//            }
//        });
        // Sự kiện cho nút Save
//        saveButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                board.save();
//                notificationLabel.setText("Bạn đã save game.");
//            }
//        });
//
//        // Sự kiện cho nút Load
//        loadButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                board.load();
//                notificationLabel.setText("Đã load game.");
//            }
//        });
    }
}
