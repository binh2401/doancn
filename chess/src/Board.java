import quanco.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import sounds.SoundPlayer;

public class Board extends JPanel {
    private final int boardWidth = 9;  // Số cột
    private final int boardHeight = 10; // Số hàng
    private final int cellSize = 70;    // Kích thước của mỗi ô
    private List<Piece> pieces;          // Danh sách các quân cờ
    private Image boardImage;            // Hình ảnh bàn cờ
    private Piece selectedPiece;          // Quân cờ đang được chọn
    private int mouseX, mouseY;          // Vị trí chuột khi kéo
    private boolean isRedTurn = true;    // Biến xác định lượt
    private Timer timer;                  // Bộ đếm thời gian
    private int timeLeft = 60;            // Thời gian còn lại
    private JLabel timerLabel;            // Nhãn hiển thị thời gian

    public Board() {
        setPreferredSize(new Dimension(boardWidth * cellSize, boardHeight * cellSize));
        pieces = new ArrayList<>();

        // Thêm quân Tướng Đỏ và Đen vào danh sách
        pieces.add(new King(4, 9, true, pieces));  // Tướng Đỏ ở vị trí (4, 9)
        pieces.add(new King(4, 0, false, pieces)); // Tướng Đen ở vị trí (4, 0)

        // Thêm các quân cờ khác...
        pieces.add(new chot(0, 6, true, pieces));
        pieces.add(new chot(0, 3, false, pieces));
        pieces.add(new chot(2, 6, true, pieces));
        pieces.add(new chot(2, 3, false, pieces));
        pieces.add(new chot(4, 6, true, pieces));
        pieces.add(new chot(4, 3, false, pieces));
        pieces.add(new chot(6, 6, true, pieces));
        pieces.add(new chot(6, 3, false, pieces));
        pieces.add(new chot(8, 6, true, pieces));
        pieces.add(new chot(8, 3, false, pieces));

        // Thêm quân xe, mã, tướng, si, pháo...
        pieces.add(new xe(0, 9, true, pieces));
        pieces.add(new xe(0, 0, false, pieces));
        pieces.add(new xe(8, 9, true, pieces));
        pieces.add(new xe(8, 0, false, pieces));
        pieces.add(new ma(1, 9, true, pieces));
        pieces.add(new ma(1, 0, false, pieces));
        pieces.add(new ma(7, 9, true, pieces));
        pieces.add(new ma(7, 0, false, pieces));
        pieces.add(new tuong(2, 9, true, pieces));
        pieces.add(new tuong(2, 0, false, pieces));
        pieces.add(new tuong(6, 9, true, pieces));
        pieces.add(new tuong(6, 0, false, pieces));
        pieces.add(new si(3, 9, true, pieces));
        pieces.add(new si(3, 0, false, pieces));
        pieces.add(new si(5, 9, true, pieces));
        pieces.add(new si(5, 0, false, pieces));
        pieces.add(new phao(1, 7, true, pieces));
        pieces.add(new phao(1, 2, false, pieces));
        pieces.add(new phao(7, 7, true, pieces));
        pieces.add(new phao(7, 2, false, pieces));

        // Khởi tạo nhãn thời gian
        timerLabel = new JLabel("Time left: " + timeLeft);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        add(timerLabel, BorderLayout.NORTH); // Thêm nhãn vào JPanel

        // Khởi động bộ đếm thời gian
        startTimer();

        // Thêm các sự kiện chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y && piece.isRed() == isRedTurn) {
                        selectedPiece = piece; // Lưu quân cờ được chọn
                        break;
                    }
                }
                mouseX = e.getX();
                mouseY = e.getY();
                repaint(); // Vẽ lại bảng cờ
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedPiece != null) {
                    int newX = e.getX() / cellSize;
                    int newY = e.getY() / cellSize;

                    // Kiểm tra xem di chuyển có hợp lệ không
                    if (selectedPiece.isValidMove(newX, newY) && newY >= 0 && newY < boardHeight) {
                        Piece targetPiece = getPieceAt(newX, newY); // Kiểm tra quân ở vị trí mới

                        if (targetPiece == null || targetPiece.isRed() != selectedPiece.isRed()) {
                            // Nếu không có quân cờ hoặc có quân địch
                            if (targetPiece != null) {
                                pieces.remove(targetPiece); // Loại bỏ quân địch
                            }
                            selectedPiece.setPosition(newX, newY); // Cập nhật vị trí quân cờ

                            // Kiểm tra chiếu tướng
                            if (isCheck(!isRedTurn)) {
                                JOptionPane.showMessageDialog(Board.this,
                                        (isRedTurn ? "Đỏ" : "Đen") + " bị chiếu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                            }

                            // Kiểm tra chiếu tướng
                            if (isCheckmate(!isRedTurn)) {
                                JOptionPane.showMessageDialog(Board.this,
                                        (isRedTurn ? "Đỏ" : "Đen") + " đã thua!", "Game Over", JOptionPane.WARNING_MESSAGE);
                                System.exit(0); // Kết thúc trò chơi
                            }

                            // Đổi lượt
                            isRedTurn = !isRedTurn;
                            timeLeft = 60; // Reset thời gian
                        }
                    }

                    // Phát âm thanh khi di chuyển quân cờ
                    SoundPlayer soundPlayer = new SoundPlayer();
                    if (selectedPiece.isRed()) {
                        soundPlayer.playSound("/sounds/move.wav"); // Âm thanh cho quân đỏ
                    } else {
                        soundPlayer.playSound("/sounds/move2.wav"); // Âm thanh cho quân đen
                    }

                    selectedPiece = null; // Đặt lại quân cờ được chọn
                    repaint(); // Vẽ lại bảng cờ
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPiece != null) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint(); // Vẽ lại để quân cờ di chuyển theo chuột
                }
            }
        });

        // Tải hình ảnh bàn cờ
        try {
            boardImage = ImageIO.read(getClass().getResourceAsStream("/img/board.gif")); // Sử dụng "/" để chỉ đường dẫn từ thư mục gốc
            if (boardImage == null) {
                System.out.println("Hình ảnh bàn cờ không thể tải!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                timerLabel.setText("Time left: " + timeLeft);
            } else {
                // Nếu thời gian hết, thông báo và kết thúc trò chơi
                JOptionPane.showMessageDialog(this, (isRedTurn ? "Đỏ" : "Đen") + " đã hết thời gian!", "Game Over", JOptionPane.WARNING_MESSAGE);
                System.exit(0); // Thoát trò chơi
            }
        });
        timer.start();
    }

    // Phương thức vẽ bàn cờ
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ hình ảnh bàn cờ
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, boardWidth * cellSize, boardHeight * cellSize, this);
        }

        // Nếu có quân cờ được chọn, vẽ dấu chấm vàng và các nước đi hợp lệ
        if (selectedPiece != null) {
            // Vẽ dấu chấm vàng cho quân cờ được chọn
            g.setColor(Color.YELLOW);
            g.fillOval(mouseX - cellSize / 4, mouseY - cellSize / 4, cellSize / 2, cellSize / 2);

            // Vẽ các nước đi hợp lệ
            for (int[] move : selectedPiece.getValidMoves()) {
                g.setColor(Color.GREEN);
                g.fillOval(move[0] * cellSize + cellSize / 4, move[1] * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
            }
        }

        // Vẽ tất cả các quân cờ
        for (Piece piece : pieces) {
            piece.draw(g, cellSize); // Thêm tham số cellSize
        }
    }

    // Kiểm tra xem quân cờ có ở vị trí nào đó hay không
    private Piece getPieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        return null; // Không có quân ở vị trí
    }

    // Kiểm tra xem một bên có bị chiếu hay không
    private boolean isCheck(boolean isRed) {
        // Tìm kiếm quân Tướng
        Piece king = null;
        for (Piece piece : pieces) {
            if (piece instanceof King && piece.isRed() == isRed) {
                king = piece;
                break;
            }
        }

        if (king == null) {
            return false; // Không tìm thấy Tướng
        }

        // Kiểm tra xem có quân cờ nào có thể tấn công quân Tướng không
        for (Piece piece : pieces) {
            if (piece.isRed() != isRed && piece.isValidMove(king.getX(), king.getY())) {
                return true; // Có quân cờ tấn công
            }
        }
        return false; // Không có quân cờ nào tấn công
    }

    // Kiểm tra xem có chiếu tướng hay không
    private boolean isCheckmate(boolean isRed) {
        // Nếu bên này bị chiếu
        if (!isCheck(isRed)) {
            return false; // Không bị chiếu
        }

        // Kiểm tra xem có nước đi hợp lệ nào không
        for (Piece piece : pieces) {
            if (piece.isRed() == isRed) {
                for (int[] move : piece.getValidMoves()) {
                    // Lưu vị trí hiện tại của quân cờ
                    int originalX = piece.getX();
                    int originalY = piece.getY();

                    // Di chuyển quân cờ
                    piece.setPosition(move[0], move[1]);

                    // Kiểm tra xem quân Tướng có bị chiếu không
                    if (!isCheck(isRed)) {
                        piece.setPosition(originalX, originalY); // Hoàn tác di chuyển
                        return false; // Có nước đi hợp lệ
                    }

                    // Hoàn tác di chuyển
                    piece.setPosition(originalX, originalY);
                }
            }
        }
        return true; // Không có nước đi hợp lệ, tức là chiếu tướng
    }
}
