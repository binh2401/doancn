package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class tuong extends Piece {
    private ImageIcon icon;

    public tuong(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi đến constructor của lớp Piece với đủ tham số
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/Tuongdo.gif" : "img/Tuongden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Kiểm tra di chuyển theo đường chéo 2 ô
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        if (dx == 2 && dy == 2) {
            // Kiểm tra giới hạn sân
            if ((isRed && newY >= 5) || (!isRed && newY < 5)) {
                // Kiểm tra chặn đường di chuyển
                int blockX = (newX + x) / 2;
                int blockY = (newY + y) / 2;
                if (getPieceAt(blockX, blockY) == null) {
                    return true;  // Hợp lệ nếu không bị chặn
                }
            }
        }
        return false;  // Không hợp lệ nếu không thỏa điều kiện
    }

    // Kiểm tra xem có quân cờ nào tại vị trí cụ thể
    @Override
    public Piece getPieceAt(int x, int y) { // Đổi quyền truy cập thành public
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
