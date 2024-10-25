package quanco;

import java.awt.Graphics;

public abstract class Piece {
    protected int x, y;
    protected boolean isRed;

    public Piece(int x, int y, boolean isRed) {
        this.x = x;
        this.y = y;
        this.isRed = isRed;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    // Phương thức để cập nhật vị trí
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isRed() {  // Thêm phương thức này
        return isRed;
    }
    public abstract boolean isValidMove(int newX, int newY);

    public abstract void draw(Graphics g, int cellSize);
}
