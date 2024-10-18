package quanco;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public class King extends Piece {

    private ImageIcon icon;

    public King(int x, int y, boolean isRed) {
        super(x, y, isRed);
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/vuado.gif" : "img/vua.gif"));
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        // Logic kiểm tra nước đi
        // ...
        return true;  // Dummy return, bạn có thể giữ logic như ban đầu
    }

    @Override
    public void draw(Graphics g, int cellSize) {
        // Lấy kích thước của ảnh quân cờ
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = x * cellSize + (cellSize - imageWidth) /2;  // Căn giữa theo trục X
        int drawY = y * cellSize + (cellSize - imageHeight) /2; // Căn giữa theo trục Y

        // Vẽ hình ảnh quân cờ tại vị trí đã tính toán
        icon.paintIcon(null, g, drawX, drawY);
    }
}
