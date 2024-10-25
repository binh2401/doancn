package quanco;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected int x, y;
    protected boolean isRed;
    protected List<Piece> pieces; // Danh sách các quân cờ

    public Piece(int x, int y, boolean isRed, List<Piece> pieces) {
        this.x = x;
        this.y = y;
        this.isRed = isRed;
        this.pieces = pieces; // Gán danh sách quân cờ
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean isValidMove(int newX, int newY);

    public abstract void draw(Graphics g, int cellSize);

    // Thêm phương thức lấy danh sách các nước đi hợp lệ
    public List<int[]> getValidMoves() {
        List<int[]> validMoves = new ArrayList<>();
        for (int newX = 0; newX < 9; newX++) {
            for (int newY = 0; newY < 10; newY++) {
                if (isValidMove(newX, newY) && newY >= 0 && newY < 10) {
                    validMoves.add(new int[]{newX, newY});
                }
            }
        }
        return validMoves;
    }

    // Kiểm tra quân cờ tại vị trí cụ thể
    protected Piece getPieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        return null;
    }
}
