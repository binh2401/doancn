import quanco.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sounds.SoundPlayer;

public class Board extends JPanel {
    private final int boardWidth = 9;  // Số cột
    private final int boardHeight = 10; // Số hàng
    private final int cellSize = 70;    // Kích thước của mỗi ô
    private List<Piece> pieces;          // Danh sách các quân cờ
    private Image boardImage;            // Hình ảnh bàn cờ
    private Piece selectedPiece;          // Quân cờ đang được chọn
    private int mouseX, mouseY;          // Vị trí chuột khi kéo

    // Constructor khởi tạo bàn cờ và quân cờ
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

        // Thêm các sự kiện chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y) {
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

                    if (selectedPiece.isValidMove(newX, newY) && newY >= 0 && newY < boardHeight) {
                        selectedPiece.setPosition(newX, newY); // Cập nhật vị trí quân cờ
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

    // Phương thức vẽ bàn cờ
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, boardWidth * cellSize, boardHeight * cellSize, this);
        }

        // Nếu có quân cờ được chọn, vẽ dấu chấm vàng và các nước đi hợp lệ
        if (selectedPiece != null) {
            // Vẽ dấu chấm vàng cho quân cờ được chọn
            g.setColor(new Color(255, 215, 0)); // Màu vàng sáng hơn
            int centerX = selectedPiece.getX() * cellSize + cellSize / 2; // Vị trí của quân cờ
            int centerY = selectedPiece.getY() * cellSize + cellSize / 2;
            g.fillOval(centerX - 10, centerY - 10, 20, 20); // Vẽ dấu chấm vàng

            // Vẽ dấu X cho các nước đi hợp lệ
            g.setColor(Color.GREEN); // Đổi màu cho dấu X thành xanh lá cây
            List<int[]> validMoves = selectedPiece.getValidMoves();
            for (int[] move : validMoves) {
                int drawX = move[0] * cellSize;
                int drawY = move[1] * cellSize;

                // Độ dài của dấu X
                int lineLength = cellSize / 3; // Ngắn hơn

                // Độ dày của dấu X
                int lineWidth = 10; // Đậm hơn

                // Vẽ dấu X
                ((Graphics2D) g).setStroke(new BasicStroke(lineWidth)); // Đặt độ dày
                g.drawLine(drawX + (cellSize - lineLength) / 2, drawY + (cellSize - lineLength) / 2,
                        drawX + (cellSize + lineLength) / 2, drawY + (cellSize + lineLength) / 2); // Đường chéo
                g.drawLine(drawX + (cellSize + lineLength) / 2, drawY + (cellSize - lineLength) / 2,
                        drawX + (cellSize - lineLength) / 2, drawY + (cellSize + lineLength) / 2); // Đường chéo
            }
        }

        for (Piece piece : pieces) {
            piece.draw(g, cellSize); // Vẽ quân cờ
        }

        // Nếu có quân cờ đang kéo, vẽ ô mờ cho quân cờ
        if (selectedPiece != null) {
            g.setColor(new Color(255, 0, 0, 150)); // Màu nền cho quân cờ đang kéo
            g.fillRect(mouseX - cellSize / 2, mouseY - cellSize / 2, cellSize, cellSize); // Vẽ ô mờ cho quân cờ
            selectedPiece.draw(g, cellSize); // Vẽ quân cờ tại vị trí chuột
        }
    }
}
