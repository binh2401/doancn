package quanco;

import javax.swing.*;
import java.awt.*;

public class ma  extends  Piece{
    private ImageIcon icon;
    public ma( int x, int y, boolean isRed){
        super(x,y,isRed);
        icon = new ImageIcon(getClass().getClassLoader().getResource(isRed ? "img/Xedo.gif" : "img/xeden.gif"));

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
        if (isRed) {
            drawY -= 0; // Điều chỉnh giá trị này cho phù hợp
        } else {
            drawY += 0; // Điều chỉnh cho quân cờ đen
        }


        icon.paintIcon(null, g, drawX, drawY);
    }

}
