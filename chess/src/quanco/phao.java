package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class phao extends Piece {
    private ImageIcon icon;
    private List<Piece> pieces; // Thêm biến `pieces` để lưu danh sách quân cờ từ lớp Board

    public phao(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed);
        this.pieces = pieces;
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/phaodo.gif" : "img/phoaden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Nếu không di chuyển theo hàng hoặc cột thì không hợp lệ
        if (x != newX && y != newY) {
            return false;
        }

        // Đếm số quân cờ giữa vị trí hiện tại và vị trí đích
        int countBetween = 0;
        if (x == newX) { // Di chuyển dọc
            int start = Math.min(y, newY) + 1;
            int end = Math.max(y, newY);
            for (int i = start; i < end; i++) {
                if (getPieceAt(x, i) != null) { // Kiểm tra quân cờ trên hàng dọc
                    countBetween++;
                }
            }
        } else if (y == newY) { // Di chuyển ngang
            int start = Math.min(x, newX) + 1;
            int end = Math.max(x, newX);
            for (int i = start; i < end; i++) {
                if (getPieceAt(i, y) != null) { // Kiểm tra quân cờ trên hàng ngang
                    countBetween++;
                }
            }
        }

        // Kiểm tra tính hợp lệ của nước đi
        Piece destinationPiece = getPieceAt(newX, newY);
        if (countBetween == 0) {
            return destinationPiece == null; // Không có quân nào giữa -> nước đi hợp lệ nếu đích không có quân
        } else if (countBetween == 1 && destinationPiece != null && destinationPiece.isRed != this.isRed) {
            return true; // Đúng một quân cờ giữa và đích có quân đối thủ -> ăn quân hợp lệ
        }

        return false; // Không hợp lệ nếu có nhiều hơn một quân ở giữa hoặc không thỏa điều kiện
    }

    private Piece getPieceAt(int x, int y) {
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
        int drawX = x * cellSize + (cellSize - imageWidth) / 2;
        int drawY = y * cellSize + (cellSize - imageHeight) / 2;

        icon.paintIcon(null, g, drawX, drawY);
    }
}
