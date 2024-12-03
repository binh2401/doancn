package giaodien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;

import AI.Board;
import auth.LoginWindow;
import auth.RegisterWindow;
import dao.UserManager;
import network.Client;
import quanco.Piece;
import sounds.BackgroundMusicPlayer;
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
    private JFrame frame; // Khai báo frame cho trò chơi AI
    private UserManager userManager;
    private boolean isAIEnabled;
    private String difficulty;
    private JButton avataname;
    private String roomId;
    private JLabel roomIdLabel;
    private String player1Name = "Người chơi 1";
    private String player2Name = "Người chơi 2";
    private String loggedInUser = null;
    private List<Piece> pieces;
    private  Board board;
    // Constructor chỉ nhận client và không còn phương thức main
    public StartWindow(Client client) {
        this.client = client;
         this.board = new Board(true, difficulty,client,roomId);
        initialize();
    }

    private void initialize() {
        // Thiết lập giao diện Nimbus trước khi tạo các thành phần GUI
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    //    musicPlayer.playBackgroundMusic("/sounds/nhacnen2.wav"); // Đường dẫn đến âm thanh nền

        JLabel title = new JLabel("Cờ Tướng AI", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);


        // Khởi tạo các nút với văn bản và hình nền
        startButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "play now");

        startButton.addActionListener(e -> {
//            if (loggedInUser == null) {
//                JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập trước khi chơi!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
            startButton.setEnabled(false); // Tạm thời vô hiệu hóa nút
            startButton.setText("Vui lòng chờ..."); // Thay đổi văn bản nút

            // Gửi yêu cầu đến server để tìm đối thủ
            client.findOpponent();

            // Xử lý khi người chơi khác đã kết nối
            client.setOnOpponentFound(() -> {
                player1Name = loggedInUser; // Tên người chơi hiện tại
                player2Name = client.getOpponentName(); // Tên đối thủ từ server

                startButton.setText("Chơi ngay"); // Đổi lại văn bản
                startButton.setEnabled(true); // Bật lại nút
                musicPlayer.stopBackgroundMusic(); // Dừng nhạc nền
                startGame();
                setVisible(false); // Ẩn auth.StartWindow
            });
        });
        playWithComputerButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "Chơi với máy");
        playWithComputerButton.addActionListener(e->{
            musicPlayer.stopBackgroundMusic();
            setVisible(false);
            startGameForAI(); // Gọi phương thức startGameForAI khi người chơi chọn "Chơi với máy"
        });

        createRoomButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "Tạo phòng");
        findTableButton = createButtonWithBackground("/img/HinhNen/btn3.jpg", "Tìm bàn chơi");

        // Đặt kích thước cho các nút
        Dimension buttonSize = new Dimension(200, 50);
        startButton.setPreferredSize(buttonSize);
        playWithComputerButton.setPreferredSize(buttonSize);
        createRoomButton.setPreferredSize(buttonSize);
        findTableButton.setPreferredSize(buttonSize);

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
        JPanel authPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Thêm hình nền cho authPanel
                try {
                    InputStream input = getClass().getResourceAsStream("/img/HinhNen/auth-background.jpg");
                    if (input != null) {
                        Image img = ImageIO.read(input);
                        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        authPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Bố trí ở bên trái
        loginButton = createStyledButton("/img/icons/login-icon.png", "Đăng nhập");
        registerButton = createStyledButton("/img/icons/register-icon.png", "Đăng ký");


        // Thêm các hành động cho nút đăng nhập
        loginButton.addActionListener(e -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);

            // Sau khi đăng nhập, kiểm tra nếu có người dùng đã đăng nhập
            loginWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
                    String username = loginWindow.getLoggedInUsername();
                    if (username != null) {
                        loggedInUser = username; // Lưu thông tin người dùng
                        updateLoginButton();    // Cập nhật giao diện
                    }
                }
            });
        });
        // Tương tự cho nút đăng ký
        registerButton.addActionListener(e -> {
            // Mở cửa sổ đăng ký và truyền userManager
            UserManager userManager = new UserManager();
            RegisterWindow registerWindow = new RegisterWindow(userManager);
            registerWindow.setVisible(true);
        });

        // Thêm nút vào panel
        authPanel.add(loginButton);
        authPanel.add(registerButton);

        // Đặt panel đăng nhập và đăng ký vào góc dưới bên trái
        add(authPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void updateLoginButton() {
        if (loggedInUser != null) {
            loginButton.setText(" " + loggedInUser); // Hiển thị tên người dùng
            loginButton.setEnabled(false); // Vô hiệu hóa nút
        }
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
        if (loggedInUser != null) {
            g.setFont(new Font("Arial", Font.BOLD, 20)); // Thiết lập font chữ
            g.setColor(Color.blue); // Thiết lập màu chữ
            g.drawString("Xin chào, " + loggedInUser, 20, 50); // Vẽ tên người dùng
        }
    }

    public void enablePlayButton() {
        startButton.setEnabled(true); // Bật nút khi nhận được tín hiệu "READY_TO_START"
    }

    public void updateBoard(String move) {

        if (board == null) {
            System.err.println("Board chưa được khởi tạo.");
            return;
        }
        move = move.trim();
        System.out.println("Raw message: '" + move + "'");
        System.out.println("Cập nhật bàn cờ với nước đi: " + move);
        if (!move.startsWith("MOVE ")) {
            move = "MOVE " + move;
            System.out.println("Thêm tiền tố 'MOVE ': '" + move + "'");
        }
        String[] parts = move.split(" ");
        if (parts.length == 5 && "MOVE".equals(parts[0])) {
            try {
                int startX = Integer.parseInt(parts[1]);
                int startY = Integer.parseInt(parts[2]);
                int endX = Integer.parseInt(parts[3]);
                int endY = Integer.parseInt(parts[4]);


                System.out.println("Đang kiểm tra quân cờ tại vị trí: (" + startX + ", " + startY + ")");
                Piece piece = board.getPieceAt(startX, startY);
                if (piece == null) {
                    System.err.println("Không tìm thấy quân cờ tại vị trí: " + startX + ", " + startY);
                    return;
                }

                System.out.println("Di chuyển quân cờ: " + piece + " từ (" + startX + ", " + startY + ") đến (" + endX + ", " + endY + ")");
                Piece capturedPiece = board.getPieceAt(endX, endY);
                if (capturedPiece != null) {
                    System.out.println("Loại bỏ quân cờ tại đích: " + capturedPiece);
                    board.getPieces().remove(capturedPiece);
                }

                piece.setPosition(endX, endY);
                board.repaint(); // Vẽ lại bàn cờ
                if (board.getParent() == null) {
                    System.err.println("Board chưa được thêm vào giao diện.");
                } else {
                    System.out.println("Board đã được thêm vào giao diện.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi định dạng nước đi: " + move);
            }
        } else {
            System.err.println("Nước đi không hợp lệ: " + move);
        }
    }


    // Phương thức bắt đầu trò chơi với AI
    public void startGameForAI() {
        if (frame == null) {
            frame = new JFrame("Đồ án cờ tướng AI"); // Khởi tạo frame nếu chưa có
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame.getContentPane().removeAll(); // Xóa cửa sổ hiện tại
        }

        // Hiển thị hộp thoại chọn độ khó AI
        String[] options = {"Dễ", "Trung bình", "Khó"};
        int choice = JOptionPane.showOptionDialog(null,
                "Chọn độ khó của AI:",
                "Chọn độ khó",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[1]); // Mặc định chọn Trung bình

        String difficulty = "medium"; // Mặc định là "Trung bình"
        switch (choice) {
            case 0:
                difficulty = "easy";
                break;
            case 1:
                difficulty = "medium";
                break;
            case 2:
                difficulty = "hard";
                break;
        }
        this.roomId= roomId;
        Client client = new Client();
        Board board = new Board(true, difficulty,client,roomId); // Truyền thông tin độ khó vào Board
        // Tạo một FunctionPanel mới cho trò chơi AI
        FunctionPanel functionPanel = new FunctionPanel(board); // Tạo FunctionPanel
        frame.add(board, BorderLayout.CENTER); // Bàn cờ ở giữa
        frame.add(functionPanel, BorderLayout.EAST); // Bảng chức năng ở bên phải

        // Sử dụng pack() để tự động điều chỉnh kích thước cửa sổ phù hợp
        frame.pack();
        frame.setVisible(true); // Hiện cửa sổ chính
        // Thiết lập JFrame hiển thị toàn màn hình
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void notifyOpponentFound() {
        // Cập nhật giao diện khi tìm thấy đối thủ
        JOptionPane.showMessageDialog(this, "Đối thủ đã được tìm thấy!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        // Kích hoạt lại nút và thay đổi tên nút
        startButton.setEnabled(true);
        startButton.setText("Chơi ngay");

        // Bắt đầu trò chơi
        startGame();
    }

    public void startGame() {
        if (frame == null) {
            frame = new JFrame("Đồ án cờ tướng"); // Khởi tạo frame nếu chưa có
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame.getContentPane().removeAll(); // Xóa cửa sổ hiện tại
        }
        this.roomId=roomId;
        Client client = new Client();
        Board board = new Board(false, difficulty, this.client, this.roomId); // Không có AI, độ khó từ tham số

        // Gán ID phòng (giả sử client đã có thông tin ID phòng từ server)

        setRoomId(roomId);

        // Tạo panel chứa thông tin người chơi và ID phòng
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BorderLayout());
        playerInfoPanel.setBackground(Color.LIGHT_GRAY);

        // Hiển thị tên người chơi 1 (bên trái)
        JLabel player1Label = new JLabel(player1Name, SwingConstants.CENTER);
        player1Label.setFont(new Font("Arial", Font.BOLD, 16));
        player1Label.setForeground(Color.BLUE);

        // Hiển thị tên người chơi 2 (bên phải)
        JLabel player2Label = new JLabel(player2Name, SwingConstants.CENTER);
        player2Label.setFont(new Font("Arial", Font.BOLD, 16));
        player2Label.setForeground(Color.RED);

        // Hiển thị ID phòng ở giữa
        roomIdLabel  = new JLabel("Phòng: " + roomId, SwingConstants.CENTER);
        roomIdLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        roomIdLabel.setForeground(Color.DARK_GRAY);

        // Thêm các thành phần vào playerInfoPanel
        playerInfoPanel.add(player1Label, BorderLayout.WEST);
        playerInfoPanel.add(roomIdLabel, BorderLayout.CENTER); // Hiển thị ID phòng ở giữa
        playerInfoPanel.add(player2Label, BorderLayout.EAST);

        // Thêm playerInfoPanel và bàn cờ vào JFrame
        frame.add(playerInfoPanel, BorderLayout.NORTH);
        frame.add(board, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true); // Hiển thị cửa sổ chính
        this.setVisible(false); // Ẩn cửa sổ start

    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;  // Lưu roomId vào biến
        System.out.println("Room ID set in StartWindow: " + roomId);

        // Kiểm tra roomIdLabel để tránh NullPointerException
        if (roomIdLabel != null) {
            roomIdLabel.setText("Room ID: " + roomId);  // Cập nhật JLabel với roomId mới
        } else {
            System.err.println("roomIdLabel is null");
        }
    }
    private JButton createStyledButton(String iconPath, String text) {
        JButton button = new JButton(text);
        try {
            InputStream input = getClass().getResourceAsStream(iconPath);
            if (input != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(input));
                button.setIcon(icon);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180)); // Màu xanh nhạt
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40)); // Kích thước chuẩn
        return button;
    }


}
