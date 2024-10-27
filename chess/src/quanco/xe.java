package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class xe extends Piece {
    private ImageIcon icon;

    public xe(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi đến constructor của lớp Piece với đầy đủ tham số
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/xedo.gif" : "img/Xeden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Kiểm tra nếu nước đi không nằm trên cùng hàng hoặc cùng cột
        if (newX != x && newY != y) {
            return false;
        }

        // Kiểm tra xem có quân nào cản đường trên cùng hàng hoặc cột
        if (newX == x) {  // Di chuyển dọc (cùng cột)
            int minY = Math.min(y, newY);
            int maxY = Math.max(y, newY);
            for (int i = minY + 1; i < maxY; i++) {
                if (getPieceAt(x, i) != null) {
                    return false;  // Có quân cản
                }
            }
        } else {  // Di chuyển ngang (cùng hàng)
            int minX = Math.min(x, newX);
            int maxX = Math.max(x, newX);
            for (int i = minX + 1; i < maxX; i++) {
                if (getPieceAt(i, y) != null) {
                    return false;  // Có quân cản
                }
            }
        }
        return true;  // Nước đi hợp lệ nếu không bị cản
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
    public void draw(Graphics g, int cellSize) {
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = x * cellSize + (cellSize - imageWidth) / 2;
        int drawY = y * cellSize + (cellSize - imageHeight) / 2;

        icon.paintIcon(null, g, drawX, drawY);
    }
}
