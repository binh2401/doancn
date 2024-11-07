import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import sounds.BackgroundMusicPlayer; // Đảm bảo bạn đã import lớp này

public class StartWindow extends JFrame {
    private JButton startButton;
    private JButton playWithComputerButton;
    private JButton createRoomButton;
    private JButton findTableButton;
    private JButton loginButton; // Nút đăng nhập
    private JButton registerButton; // Nút đăng ký
    private Image backgroundImage;
    private BackgroundMusicPlayer musicPlayer; // Biến cho lớp âm thanh
    private Client client;
    private  Main main;
    private boolean isAIEnabled;
    private String difficulty;

    public StartWindow(Main main, Client client) {
        this.client = client;
        this.main = main;
        initialize();
    }
    private void initialize() {
        setTitle("Chào mừng đến với Cờ Tướng AI");
        setSize(700, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tải hình nền
        try {
            InputStream input = getClass().getResourceAsStream("/img/HinhNen/choi-co-tuong-voi-may.jpg");
            if (input == null) {
                throw new IOException("Không tìm thấy hình ảnh: /img/HinhNen/choi-co-tuong-voi-may.jpg");
            }
            backgroundImage = ImageIO.read(input);
        } catch (IOException e) {
            System.out.println("Không thể tải hình ảnh nền");
            e.printStackTrace();
        }

        // Khởi tạo âm thanh nền
        musicPlayer = new BackgroundMusicPlayer();
        musicPlayer.playBackgroundMusic("/sounds/nhacNen.wav"); // Đường dẫn đến âm thanh nền


        JLabel title = new JLabel("Cờ Tướng AI", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Khởi tạo các nút với văn bản và hình nền
        startButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "play now");

        startButton.addActionListener(e -> {
            startButton.setEnabled(false); // Tạm thời vô hiệu hóa nút
            startButton.setText("Vui lòng chờ..."); // Thay đổi văn bản nút

            // Gửi yêu cầu đến server để tìm đối thủ
            client.findOpponent();

            // Xử lý khi người chơi khác đã kết nối
            client.setOnOpponentFound(() -> {
                startButton.setText("Chơi ngay"); // Đổi lại văn bản
                startButton.setEnabled(true); // Bật lại nút
                musicPlayer.stopBackgroundMusic(); // Dừng nhạc nền
                setVisible(false); // Ẩn StartWindow
                if (main != null) { // Kiểm tra xem main có phải là null không
                    main.startGameForPlayer(difficulty); // Khởi động trò chơi
                } else {
                    System.out.println("Main is null!");
                }
            });
        });
        playWithComputerButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "Chơi với máy");
        playWithComputerButton.addActionListener(e->{
            musicPlayer.stopBackgroundMusic();
            setVisible(false);
            main.startGameForAI();
        });
        createRoomButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "Tạo phòng");
        findTableButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "Tìm bàn chơi");

        // Đặt kích thước cho các nút
        Dimension buttonSize = new Dimension(200, 50);
        startButton.setPreferredSize(buttonSize);
        playWithComputerButton.setPreferredSize(buttonSize);
        createRoomButton.setPreferredSize(buttonSize);
        findTableButton.setPreferredSize(buttonSize);

        // Thêm hành động cho các nút
//        startButton.addActionListener(e -> {
//            musicPlayer.stopBackgroundMusic(); // Dừng nhạc nền
//            setVisible(false); // Ẩn StartWindow
//            main.startGame(); // Khởi động trò chơi
//        });

        // Uncomment and implement these actions if needed
        // playWithComputerButton.addActionListener(e -> {
        //     main.playWithComputer(); // Gọi phương thức chơi với máy
        // });
        // createRoomButton.addActionListener(e -> {
        //     main.createRoom(); // Gọi phương thức tạo phòng
        // });
        // findTableButton.addActionListener(e -> {
        //     main.findTable(); // Gọi phương thức tìm bàn chơi
        // });

        // Tạo panel chứa các nút và đặt chúng vào giữa cửa sổ
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0); // Khoảng cách giữa các nút

        buttonPanel.add(startButton, gbc);
        buttonPanel.add(playWithComputerButton, gbc);
        buttonPanel.add(createRoomButton, gbc);
        buttonPanel.add(findTableButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        // Tạo panel cho các nút đăng nhập và đăng ký
        JPanel authPanel = new JPanel();
        authPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Bố trí ở bên trái
        loginButton = new JButton("Đăng nhập");
        registerButton = new JButton("Đăng ký");

        // Đặt kích thước cho các nút đăng nhập và đăng ký
        loginButton.setPreferredSize(new Dimension(100, 30));
        registerButton.setPreferredSize(new Dimension(100, 30));

        // Thêm nút vào panel
        authPanel.add(loginButton);
        authPanel.add(registerButton);

        // Đặt panel đăng nhập và đăng ký vào góc dưới bên trái
        add(authPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Phương thức tạo nút với hình nền và văn bản
    private JButton createButtonWithBackground(String imagePath, String text) {
        JButton button = new JButton();
        try {
            InputStream input = getClass().getResourceAsStream(imagePath);
            if (input == null) {
                throw new IOException("Không tìm thấy hình ảnh: " + imagePath);
            }
            ImageIcon icon = new ImageIcon(ImageIO.read(input));

            // Điều chỉnh kích thước hình ảnh theo kích thước nút
            Image scaledImage = icon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));

            // Thiết lập văn bản cho nút
            button.setText(text);
            button.setHorizontalTextPosition(SwingConstants.CENTER); // Đặt vị trí văn bản
            button.setVerticalTextPosition(SwingConstants.CENTER);   // Đặt vị trí văn bản
            button.setFont(new Font("Arial", Font.BOLD, 16)); // Cỡ chữ
            button.setForeground(Color.WHITE); // Màu chữ

            button.setPreferredSize(new Dimension(200, 50));
            button.setContentAreaFilled(false); // Không tô màu nền cho nút
            button.setBorderPainted(false); // Không vẽ viền cho nút
            button.setFocusPainted(false); // Không tô viền khi chọn nút
        } catch (Exception e) {
            System.out.println("Không thể tải hình ảnh: " + imagePath);
            e.printStackTrace();
        }
        return button;
    }

    // Phương thức vẽ hình nền
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    public void enablePlayButton() {
        startButton.setEnabled(true); // Bật nút khi nhận được tín hiệu "READY_TO_START"
    }
    public void updateBoard(String move) {
        System.out.println("Cập nhật bàn cờ với nước đi: " + move);
        // Cập nhật GUI hoặc bàn cờ ở đây
    }
    // Phương thức đóng cửa sổ
    @Override
    public void dispose() {
        super.dispose();
        // Không cần dừng nhạc nền ở đây nữa
    }
}
