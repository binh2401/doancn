package quanco;

import AI.Board;
import AI.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected int x, y; // Tọa độ quân cờ
    protected boolean isRed; // True nếu quân cờ đỏ, false nếu quân cờ đen
    protected List<Piece> pieces; // Danh sách các quân cờ
    private boolean captured = false; // Trạng thái bị bắt

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }
    public Piece(int x, int y, boolean isRed, List<Piece> pieces) {
        this.x = x;
        this.y = y;
        this.isRed = isRed; // Gán màu cho quân cờ
        this.pieces = pieces; // Gán danh sách quân cờ
    }

    public int getX() {
        return x; // Trả về tọa độ x
    }

    public int getY() {
        return y; // Trả về tọa độ y
    }

    public void setPosition(int x, int y) {
        this.x = x; // Cập nhật tọa độ x
        this.y = y; // Cập nhật tọa độ y
    }

    public boolean isRed() {
        return isRed; // Trả về màu của quân cờ
    }

    public abstract boolean isValidMove(int newX, int newY); // Kiểm tra tính hợp lệ của nước đi

    public abstract void draw(Graphics g, int cellSize,int boardX, int boardY); // Phương thức vẽ quân cờ

    // Phương thức lấy danh sách các nước đi hợp lệ
    public List<int[]> getValidMoves() {
        List<int[]> validMoves = new ArrayList<>();
        for (int newX = 0; newX < 9; newX++) { // Duyệt qua tất cả các ô cờ
            for (int newY = 0; newY < 10; newY++) {
                if (isValidMove(newX, newY) && newY >= 0 && newY < 10) {
                    // Kiểm tra nếu nước đi hợp lệ và trong giới hạn của bàn cờ
                    Piece targetPiece = getPieceAt(newX, newY); // Lấy quân cờ tại vị trí mới
                    if (targetPiece == null || targetPiece.isRed() != this.isRed()) {
                        // Chỉ thêm nước đi hợp lệ nếu ô trống hoặc chứa quân địch
                        validMoves.add(new int[]{newX, newY});
                    }
                }
            }
        }
        return validMoves; // Trả về danh sách các nước đi hợp lệ
    }

    // Kiểm tra quân cờ tại vị trí cụ thể
    protected Piece getPieceAt(int x, int y) {
        // Kiểm tra nếu quân cờ tại vị trí đã cho
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece; // Trả về quân cờ tại vị trí (x, y)
            }
        }
        return null; // Không có quân cờ tại vị trí
    }
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        // Logic để tính toán các nước đi hợp lệ dựa trên trạng thái của bàn cờ
        return validMoves;
    }

    public void setPosition(Move.Position startPosition) {
        this.x = startPosition.getX(); // Lấy tọa độ x từ startPosition
        this.y = startPosition.getY();
    }
}
