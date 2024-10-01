import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    private final int boardWidth = 9;  // Số cột
    private final int boardHeight = 11; // Số hàng
    private final int cellSize = 80;   // Kích thước của mỗi ô
    private List<Piece> pieces;  // Danh sách các quân cờ

    // Constructor khởi tạo bàn cờ và quân cờ
    public Board() {
        setPreferredSize(new Dimension(boardWidth * cellSize, boardHeight * cellSize));

        // Khởi tạo danh sách quân cờ
        pieces = new ArrayList<>();

        // Thêm quân Tướng Đỏ và Đen vào danh sách
        pieces.add(new King(4, 9, true));  // Tướng Đỏ ở vị trí (4, 9)
        pieces.add(new King(7/2, 0, false)); // Tướng Đen ở vị trí (4, 0)
    }

    // Phương thức vẽ bàn cờ
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ các đường ngang
        for (int row = 0; row < boardHeight; row++) {
            if (row != 5) { // Không vẽ đường ngang ở Sông
                g.drawLine(0, row * cellSize, (boardWidth - 1) * cellSize, row * cellSize);
            } else { // Vẽ đường sông, bỏ trống
                g.drawLine(0, row * cellSize, 2 * cellSize, row * cellSize); // Đoạn đầu
                g.drawLine(7 * cellSize, row * cellSize, (boardWidth - 1) * cellSize, row * cellSize); // Đoạn cuối
            }
        }

        // Vẽ các đường dọc
        for (int col = 0; col < boardWidth; col++) {
            g.drawLine(col * cellSize, 0, col * cellSize, 4 * cellSize); // Phần trên của bàn cờ
            g.drawLine(col * cellSize, 6 * cellSize, col * cellSize, (boardHeight - 1) * cellSize); // Phần dưới của bàn cờ
        }

        // Vẽ đường chéo trong cung của Đỏ
        g.drawLine(3 * cellSize, 8 * cellSize, 5 * cellSize, 10 * cellSize);
        g.drawLine(5 * cellSize, 8 * cellSize, 3 * cellSize, 10 * cellSize);

        // Vẽ đường chéo trong cung của Đen
        g.drawLine(3 * cellSize, 0, 5 * cellSize, 2 * cellSize);
        g.drawLine(5 * cellSize, 0, 3 * cellSize, 2 * cellSize);

        // Thêm nhãn hoặc ký hiệu khác (như "Sông")
        g.drawString("Sông", 4 * cellSize - 10, 5 * cellSize - 5);

        // Vẽ các quân cờ
        for (Piece piece : pieces) {
            piece.draw(g, cellSize);
        }
    }
}
