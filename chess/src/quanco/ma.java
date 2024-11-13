package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ma extends Piece {
    private ImageIcon icon;

    public ma(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi đến constructor của lớp Piece với đầy đủ tham số
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/Mado.gif" : "img/Maden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Tính toán độ lệch giữa vị trí hiện tại và vị trí đích
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        // Kiểm tra điều kiện nước đi hình chữ "L" của quân Mã
        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            // Kiểm tra quân chặn theo hướng ban đầu
            int blockX = x + (dx == 2 ? (newX - x) / 2 : 0);
            int blockY = y + (dy == 2 ? (newY - y) / 2 : 0);

            // Nếu có quân cờ chặn đường, nước đi không hợp lệ
            if (getPieceAt(blockX, blockY) != null) {
                return false;
            }
            // Nước đi hợp lệ nếu không có quân chặn
            return true;
        }
        return false;
    }

    // Phương thức kiểm tra quân cờ tại vị trí cụ thể
    @Override
    public Piece getPieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics g, int cellSize,int boardX, int boardY) {
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = (x * cellSize) + (cellSize - imageWidth) / 2 + boardX; // Cộng boardX
        int drawY = (y * cellSize) + (cellSize - imageHeight) / 2 + boardY; // Cộng boardY

        icon.paintIcon(null, g, drawX, drawY);
    }
}
