import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartWindow extends JFrame {
    private JButton startButton;
    private Image backgroundImage; // Thêm biến hình nền

    public StartWindow(Main main) {
        setTitle("Chào mừng đến với Cờ Tướng AI");
        setSize(700, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tải hình nền
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/img/HinhNen/choi-co-tuong-voi-may.jpg")); // Đảm bảo đường dẫn đúng
            if (backgroundImage == null) {
                System.out.println("Hình ảnh bàn cờ không thể tải!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel title = new JLabel("Cờ Tướng AI", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH); // Đặt tiêu đề ở phía trên

        // Khởi tạo nút bắt đầu
        startButton = new JButton("Bắt đầu chơi");
        startButton.setPreferredSize(new Dimension(200, 50)); // Đặt kích thước cho nút
        startButton.addActionListener(e -> {
            setVisible(false); // Ẩn cửa sổ bắt đầu
            main.startGame(); // Gọi phương thức startGame trong Main
        });

        // Thay đổi vị trí nút
        JPanel buttonPanel = new JPanel(); // Tạo một JPanel để chứa nút
        buttonPanel.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout

        // Thay đổi GridBagConstraints để căn giữa
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa
        buttonPanel.add(startButton, gbc); // Thêm nút vào panel

        add(buttonPanel, BorderLayout.CENTER); // Thêm panel vào giữa cửa sổ

        setLocationRelativeTo(null); // Đặt cửa sổ ở giữa màn hình
        setVisible(true);
    }

    // Phương thức vẽ hình nền
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
