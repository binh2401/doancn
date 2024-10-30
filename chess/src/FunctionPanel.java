import javax.swing.*;
import java.awt.*;

public class FunctionPanel extends JPanel {
    public FunctionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Sắp xếp các thành phần theo chiều dọc

        // Tạo các nút chức năng
        JButton resetButton = new JButton("Reset Game");
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");

        // Thêm các nút vào panel
        add(resetButton);
        add(saveButton);
        add(loadButton);

        // Tạo nhãn thông báo
        JLabel notificationLabel = new JLabel("Thông báo sẽ hiển thị ở đây");
        add(notificationLabel);
    }
}
