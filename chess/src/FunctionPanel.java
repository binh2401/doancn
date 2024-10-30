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
    }
}
