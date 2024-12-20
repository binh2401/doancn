package giaodien;

import AI.Board;
import network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FunctionPanel extends JPanel {
    private Board board; // Biến để lưu tham chiếu đến Board
    private Client client;
    // Phương thức tạo nút với hình ảnh
    private JButton createImageButton(String imagePath, String tooltip) {
        JButton button = new JButton();
        this.client = client;
        try {
            // Tải hình ảnh từ tài nguyên
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            // Điều chỉnh kích thước hình ảnh
            int buttonWidth = 70;
            int buttonHeight = 40;
            Image scaledImage = icon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage)); // Đặt biểu tượng cho nút
        } catch (Exception e) {
            System.err.println("Không thể tải hình ảnh từ đường dẫn: " + imagePath);
            e.printStackTrace();
        }
        button.setToolTipText(tooltip);
        button.setContentAreaFilled(false); // Loại bỏ nền mặc định
        button.setBorderPainted(false); // Loại bỏ viền mặc định
        button.setFocusPainted(false); // Loại bỏ hiệu ứng khi chọn
        button.setPreferredSize(new Dimension(120, 40)); // Đặt kích thước cố định
        return button;
    }

    public FunctionPanel(Board board) {
        this.board = board; // Lưu tham chiếu đến Board

        setLayout(new BorderLayout()); // Sử dụng BorderLayout

        // Panel để chứa các nút chức năng và nhãn thông báo
        JPanel buttonPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon(getClass().getResource("/img/HinhNen/nen.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Vẽ hình nền với kích thước bằng với panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Sắp xếp theo chiều dọc
        buttonPanel.setPreferredSize(new Dimension(200, 0)); // Chiều rộng cố định

        // Tạo các nút chức năng với kích thước cố định
        JButton resetButton = createImageButton("/img/HinhNen/reset.png", "Reset Game");
        JButton surrenderButton = createImageButton("/img/HinhNen/surrender.png", "Surrender");
       // JButton drawButton = createImageButton("/img/HinhNen/peace.png", "Draw");
        JButton backButton = createImageButton("/img/HinhNen/rollback.png", "Roll Back");
//        JButton saveButton = new JButton("Save Game");

        //nut out phong`
        JButton loadButton = createImageButton("/img/HinhNen/home.png", "Rời khỏi");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tạo cửa sổ StartWindow
                board.stopBoard();
                JFrame startWindow = new StartWindow(client); // Giả sử bạn đã có lớp StartWindow mở giao diện chính

                // Hiển thị cửa sổ StartWindow
                startWindow.setVisible(true);

                // Đóng cửa sổ hiện tại
                SwingUtilities.getWindowAncestor(FunctionPanel.this).dispose();
            }
        });
        // Đặt kích thước cố định cho các nút
        Dimension buttonSize = new Dimension(120, 40);
        resetButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        surrenderButton.setPreferredSize(buttonSize);
      //  drawButton.setPreferredSize(buttonSize);
//        saveButton.setPreferredSize(buttonSize);
//        loadButton.setPreferredSize(buttonSize);

        // Đảm bảo nút không bị giãn
        resetButton.setMaximumSize(buttonSize);
        surrenderButton.setMaximumSize(buttonSize);
    //    drawButton.setMaximumSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
//        saveButton.setPreferredSize(buttonSize);
       loadButton.setPreferredSize(buttonSize);

        // Đặt căn chỉnh cho các nút (giữa theo chiều ngang)
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        surrenderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      //  drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm các nút vào buttonPanel
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(surrenderButton);
        buttonPanel.add(Box.createVerticalStrut(10));
 //       buttonPanel.add(drawButton);
        buttonPanel.add(Box.createVerticalStrut(10));


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
//        buttonPanel.add(saveButton);
//        buttonPanel.add(Box.createVerticalStrut(10));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(loadButton);





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
    }
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

