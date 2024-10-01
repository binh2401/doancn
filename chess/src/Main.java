import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Tạo cửa sổ JFrame
        JFrame frame = new JFrame("Đồ án cờ tướng AI");

        // Đặt layout của JFrame là GridBagLayout để có thể canh giữa
        frame.setLayout(new GridBagLayout());

        // Tạo đối tượng Board để vẽ bàn cờ
        Board board = new Board();

        // Tạo GridBagConstraints để canh giữa bàn cờ
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Đảm bảo bàn cờ nằm giữa

        // Thêm đối tượng Board vào JFrame với vị trí đã định
        frame.add(board, gbc);

        // Sử dụng pack() để tự động điều chỉnh kích thước cửa sổ phù hợp với bàn cờ
        frame.pack();

        // Đảm bảo chương trình dừng khi đóng cửa sổ
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hiển thị cửa sổ
        frame.setVisible(true);
    }
}
