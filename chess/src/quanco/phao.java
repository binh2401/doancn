package quanco;

import javax.swing.*;
import java.awt.*;

public class phao extends  Piece{
    private ImageIcon icon;
    public phao (int x, int y, boolean isRed){
        super(x,y,isRed);
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/phaodo.gif":"img/phoaden.gif"));
    }
    @Override
    public boolean isValidMove( int newX, int newY){
        return true;
    }
    public  void draw(Graphics g, int cellSize){
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // Tính toán tọa độ để căn giữa quân cờ trong ô
        int drawX = x * cellSize + (cellSize - imageWidth) /2;  // Căn giữa theo trục X
        int drawY = y * cellSize + (cellSize - imageHeight) /2; // Căn giữa theo trục Y
        // Điều chỉnh tọa độ để căn giữa chính xác
        // Điều chỉnh tọa độ cho quân cờ đỏ nếu cần

        if (this.isRed) {
            drawY -= 0 * cellSize; // Giảm 0.25 ô cho quân đỏ
        }


        if (!this.isRed) {
            if (y < 5) {
                drawY += 0 * cellSize; // Tăng 0.25 ô nếu quân đen chưa qua sông
            }

        }

        icon.paintIcon(null, g, drawX, drawY);
    }

}
