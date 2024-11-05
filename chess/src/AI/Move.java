package AI;

import quanco.Piece;

import javax.swing.text.Position;

public class Move {
    private final Piece piece; // Quân cờ đã thực hiện nước đi
    private final int oldX, oldY; // Vị trí cũ
    private final int newX, newY; // Vị trí mới
    private final Piece capturedPiece; // Quân cờ bị bắt

    public Move(Piece piece, int oldX, int oldY, int newX, int newY, Piece capturedPiece) {
        this.piece = piece; // Gán quân cờ đã di chuyển
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.capturedPiece = capturedPiece; // Gán quân cờ bị bắt
    }

    public Piece getMovedPiece() {
        return piece; // Trả về quân cờ đã di chuyển
    }

    public Position getStartPosition() {
        return new Position(oldX, oldY); // Trả về vị trí bắt đầu
    }

    // Phương thức để lấy vị trí kết thúc
    public Position getEndPosition() {
        return new Position(newX, newY); // Trả về vị trí kết thúc
    }

    public Piece getPiece() {
        return piece;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Getter cho x và y
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}