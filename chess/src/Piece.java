import java.awt.Graphics;

public abstract class Piece {
    protected int x, y;
    protected boolean isRed;

    public Piece(int x, int y, boolean isRed) {
        this.x = x;
        this.y = y;
        this.isRed = isRed;
    }

    public abstract boolean isValidMove(int newX, int newY);

    public abstract void draw(Graphics g, int cellSize);
}
