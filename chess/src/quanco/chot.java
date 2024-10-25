package quanco;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.util.List;

public class chot extends Piece {
    private ImageIcon icon;

    public chot(int x, int y, boolean isRed, List<Piece> pieces) {
        super(x, y, isRed, pieces); // Gọi constructor của lớp cha
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/chotdo.gif" : "img/chotden.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Nếu quân cờ đang ở phía trên sông (y < 5)
        if (!isRed) { // Quân đen
            if (y < 5) {
                return (newX == x && newY == y + 1);
            } else {
                return (newX == x && newY == y + 1) || (Math.abs(newX - x) == 1 && newY == y);
            }
        } else { // Quân đỏ
            if (y > 4) {
                return (newX == x && newY == y - 1);
            } else {
                return (newX == x && newY == y - 1) || (Math.abs(newX - x) == 1 && newY == y);
            }
        }
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
