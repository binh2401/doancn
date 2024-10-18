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
        // Kiểm tra điều kiện di chuyển qua sông
        if (this.isRed) {
            // Ví dụ: Quân cờ đỏ không thể đi xuống dưới hàng 5
            return newY <= 5;
        } else {
            // Ví dụ: Quân cờ đen không thể đi lên trên hàng 5
            return newY >= 5;
        }
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
            drawY -= 25; // Điều chỉnh giá trị này cho phù hợp
        } else {
            drawY += 25; // Điều chỉnh cho quân cờ đen
        }


        icon.paintIcon(null, g, drawX, drawY);
    }
}
