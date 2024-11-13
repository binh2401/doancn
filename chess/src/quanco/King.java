package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class King extends Piece {
    private ImageIcon icon;

    public King(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi đến constructor của lớp Piece với đầy đủ tham số
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/vuado.gif" : "img/vua.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Kiểm tra nếu di chuyển nằm ngoài bàn cờ
        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 10) {
            return false;
        }

        // Kiểm tra di chuyển một ô ngang hoặc dọc
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);
        if (dx + dy != 1) {
            return false; // Tướng chỉ có thể di chuyển 1 ô theo chiều ngang hoặc dọc
        }

        // Kiểm tra phạm vi Cửu cung
        if (isRed) {
            return newX >= 3 && newX <= 5 && newY >= 7 && newY <= 9;
        } else {
            return newX >= 3 && newX <= 5 && newY >= 0 && newY <= 2;
        }
    }

    @Override
    public void draw(Graphics g, int cellSize,int boardX, int boardY) {
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = (x * cellSize) + (cellSize - imageWidth) / 2 + boardX; // Cộng boardX
        int drawY = (y * cellSize) + (cellSize - imageHeight) / 2 + boardY; // Cộng boardY

        if (this.isRed && y >= 7) {
            drawY -= 0.05 * cellSize;
        } else if (!this.isRed && y <= 2) {
            drawY += 0.05 * cellSize;
        }

        icon.paintIcon(null, g, drawX, drawY);
    }
}
