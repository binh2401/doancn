package quanco;
import java.awt.Graphics;
import javax.swing.ImageIcon;
public class chot extends Piece {
    private ImageIcon icon;
    public chot(int x, int y, boolean isRed){
        super(x,y,isRed);
        icon  = new ImageIcon(getClass().getClassLoader().getResource(isRed? "img/chotdo.gif" : "img/chotden.gif"));

    }
    @Override
    public boolean isValidMove(int newX, int newY) {
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
            if (y==6){
                drawY -=0.05 * cellSize;
            }



        }

        if ((this.isRed && y <= 5) ) {
            drawY -= 0 * cellSize; // Giảm 0.25 ô nếu quân đã qua sông
        }





        if (!this.isRed) {
            if (y == 3) {
                drawY += 0 * cellSize; // Tăng 0.25 ô nếu quân đen chưa qua sông
            }
        }

        icon.paintIcon(null, g, drawX, drawY);
    }
}
