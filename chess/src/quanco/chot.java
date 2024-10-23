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
            drawY -= 0.35 * cellSize; // Giảm 0.25 ô cho quân đỏ
            if (y==6){
                drawY -=0.05 * cellSize;
            }



        }

        if ((this.isRed && y <= 5) ) {
            drawY -= 0.15 * cellSize; // Giảm 0.25 ô nếu quân đã qua sông
        }
        if ((this.isRed && y<=4)){
            drawY -=0.15 * cellSize;
        }
        if ((this.isRed && y<=3)){
            drawY -=0.05 * cellSize;
        }
        if ((this.isRed && y<=2)){
            drawY -=0.05 * cellSize;
        }




        if (!this.isRed) {
            if (y == 3) {
                drawY += 0.35 * cellSize; // Tăng 0.25 ô nếu quân đen chưa qua sông
            }
            if (y==4){
                drawY += 0.45 * cellSize;
            }
        }
        if ( (!this.isRed && y >= 6)) {
            drawY -=0.5* cellSize;
        }
        if ( (!this.isRed && y >= 7)) {
            drawY +=0.1* cellSize;
        }
        if ( (!this.isRed && y >= 8)) {
            drawY +=0.1* cellSize;
        }
        if ( (!this.isRed && y >= 9)) {
            drawY +=0.1* cellSize;
        }

        icon.paintIcon(null, g, drawX, drawY);
    }
}
