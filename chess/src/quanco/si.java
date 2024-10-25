package quanco;

import javax.swing.*;
import java.awt.*;

public class si extends Piece {
    private ImageIcon icon;

    public si(int x, int y, boolean isRed) {
        super(x, y, isRed);
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/sido.gif" : "img/siden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Kiểm tra xem quân có di chuyển trong khu vực hợp lệ không
        if (isRed) {
            // Quân đỏ: chỉ có thể di chuyển trong khu vực hàng 7, 8, 9
            if (newY < 7 || newY > 9) {
                return false; // Bên đen
            }
        } else {
            // Quân đen: chỉ có thể di chuyển trong khu vực hàng 0, 1, 2
            if (newY < 0 || newY > 2) {
                return false; // Bên đỏ
            }
        }

        // Kiểm tra di chuyển chéo
        if ((newX == x - 1 && newY == y - 1) || (newX == x + 1 && newY == y - 1) ||
                (newX == x - 1 && newY == y + 1) || (newX == x + 1 && newY == y + 1)) {
            return true; // Nước đi hợp lệ
        }

        return false; // Nước đi không hợp lệ
    }

    public void draw(Graphics g, int cellSize) {
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = x * cellSize + (cellSize - imageWidth) / 2; // Căn giữa theo trục X
        int drawY = y * cellSize + (cellSize - imageHeight) / 2; // Căn giữa theo trục Y

        // Điều chỉnh tọa độ cho quân cờ đỏ nếu cần
        if (this.isRed) {
            drawY -= 0 * cellSize; // Giảm 0 ô cho quân đỏ
        }

        if (!this.isRed) {
            if (y < 5) {
                drawY += 0 * cellSize; // Tăng 0 ô nếu quân đen chưa qua sông
            }
        }

        icon.paintIcon(null, g, drawX, drawY);
    }
}
