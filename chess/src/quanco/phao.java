package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class phao extends Piece {
    private ImageIcon icon;

    public phao(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi constructor của lớp cha
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/phaodo.gif" : "img/phoaden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Kiểm tra xem di chuyển có theo hàng hoặc cột không
        if (x != newX && y != newY) {
            return false; // Nước đi không hợp lệ nếu không theo hàng hoặc cột
        }

        // Đếm số quân cờ giữa vị trí hiện tại và vị trí đích
        int countBetween = 0;
        if (x == newX) { // Di chuyển dọc
            int start = Math.min(y, newY) + 1;
            int end = Math.max(y, newY);
            for (int i = start; i < end; i++) {
                if (getPieceAt(x, i) != null) {
                    countBetween++;
                }
            }
        } else if (y == newY) { // Di chuyển ngang
            int start = Math.min(x, newX) + 1;
            int end = Math.max(x, newX);
            for (int i = start; i < end; i++) {
                if (getPieceAt(i, y) != null) {
                    countBetween++;
                }
            }
        }

        // Kiểm tra tính hợp lệ của nước đi
        Piece destinationPiece = getPieceAt(newX, newY);
        if (countBetween == 0) {
            // Nếu không có quân nào ở giữa và đích không có quân
            return destinationPiece == null;
        } else if (countBetween == 1 && destinationPiece != null && destinationPiece.isRed != isRed) {
            // Nếu có một quân ở giữa và đích có quân đối thủ
            return true;
        }

        return false; // Nước đi không hợp lệ
    }

    @Override
    public void draw(Graphics g, int cellSize) {
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();
        int drawX = x * cellSize + (cellSize - imageWidth) / 2; // Căn giữa theo trục X
        int drawY = y * cellSize + (cellSize - imageHeight) / 2; // Căn giữa theo trục Y

        icon.paintIcon(null, g, drawX, drawY);
    }
}
