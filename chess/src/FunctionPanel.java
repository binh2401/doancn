import AI.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FunctionPanel extends JPanel {
    private Board board; // Biến để lưu tham chiếu đến Board

    public FunctionPanel(Board board) {
        this.board = board; // Lưu tham chiếu đến Board

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Sắp xếp các thành phần theo chiều dọc

        // Tạo các nút chức năng
        JButton resetButton = new JButton("Reset Game");
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JButton backButton = new JButton("Roll Back");

        // Thêm các nút vào panel
        add(resetButton);
        add(saveButton);
        add(loadButton);
        add(backButton);

        // Tạo nhãn thông báo
        JLabel notificationLabel = new JLabel("Thông báo sẽ hiển thị ở đây");
        add(notificationLabel);

        // Sự kiện cho nút Roll Back
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra nếu có thể quay lại nước đi trước đó
                if (board.undoLastMovePair()) {
                    notificationLabel.setText("Quân cờ đã quay lại nước đi trước đó.");
                } else {
                    notificationLabel.setText("Không thể quay lại nước đi.");
                }
            }
        });

        // Các sự kiện khác cho reset, save, load sẽ được thêm vào đây
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gọi phương thức reset game
                board.resetGame();
                notificationLabel.setText("Trò chơi đã được khởi động lại.");
            }
        });


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save game logic here
                notificationLabel.setText("Trò chơi đã được lưu.");
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load game logic here
                notificationLabel.setText("Trò chơi đã được tải.");
            }
        });
    }
}
