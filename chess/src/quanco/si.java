package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class si extends Piece {
    private ImageIcon icon;

    public si(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi constructor của lớp cha
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
            // Kiểm tra có quân nào cản đường không (nếu cần)
            if (getPieceAt(newX, newY) != null) {
                return false; // Có quân cản đường
            }
            return true; // Nước đi hợp lệ
        }

        return false; // Nước đi không hợp lệ
    }

    @Override
    public void draw(Graphics g, int cellSize) {
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = x * cellSize + (cellSize - imageWidth) / 2; // Căn giữa theo trục X
        int drawY = y * cellSize + (cellSize - imageHeight) / 2; // Căn giữa theo trục Y

        icon.paintIcon(null, g, drawX, drawY);
    }
}
