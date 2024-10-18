import quanco.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    private final int boardWidth = 9;  // Số cột
    private final int boardHeight = 11; // Số hàng
    private final int cellSize = 80;   // Kích thước của mỗi ô
    private List<Piece> pieces;  // Danh sách các quân cờ
    private Image boardImage;  // Hình ảnh bàn cờ

    // Constructor khởi tạo bàn cờ và quân cờ
    public Board() {
        setPreferredSize(new Dimension(boardWidth * cellSize, boardHeight * cellSize));

        // Khởi tạo danh sách quân cờ
        pieces = new ArrayList<>();

        // Thêm quân Tướng Đỏ và Đen vào danh sách
        pieces.add(new King(4, 10, true));  // Tướng Đỏ ở vị trí (4, 9)
        pieces.add(new King(4, 0, false)); // Tướng Đen ở vị trí (4, 0)

        // quan chốt
        pieces.add(new chot(0,7,true));
        pieces.add(new chot(0,3,false));

        pieces.add(new chot(2,7,true));
        pieces.add(new chot(2,3,false));

        pieces.add(new chot(4,7,true));
        pieces.add(new chot(4,3,false));

        pieces.add(new chot(6,7,true));
        pieces.add(new chot(6,3,false));

        pieces.add(new chot(8,7,true));
        pieces.add(new chot(8,3,false));

        // quan xe
        pieces.add((new xe(0,10,true)));
        pieces.add(new xe(0,0,false));

        pieces.add((new xe(8,10,true)));
        pieces.add(new xe(8,0,false));
        // ma

        pieces.add(new ma(1,10,true));
        pieces.add(new ma(1,0,false));

        pieces.add(new ma(7,10,true));
        pieces.add(new ma(7,0,false));

        // tuong

        pieces.add(new tuong(2,10,true));
        pieces.add(new tuong(2,0 ,false));

        pieces.add(new tuong(6,10,true));
        pieces.add(new tuong(6,0,false));

        //si
        pieces.add(new si(3,10,true));
        pieces.add(new si(3,0,false));

        pieces.add(new si(5,10,true));
        pieces.add(new si(5,0,false));

        //phao

        pieces.add(new phao(1,8,true));
        pieces.add(new phao(1,2,false));

        pieces.add(new phao(7,8,true));
        pieces.add(new phao(7,2,false));


        // Tải hình ảnh bàn cờ
        try {
            boardImage = ImageIO.read(getClass().getResourceAsStream("/img/board.gif")); // Sử dụng "/" để chỉ đường dẫn từ thư mục gốc
            if (boardImage == null) {
                System.out.println("Hình ảnh bàn cờ không thể tải!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Phương thức vẽ bàn cờ
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ hình bàn cờ nếu nó không null
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, boardWidth * cellSize, boardHeight * cellSize, this);
        }

        // Vẽ các quân cờ
        for (Piece piece : pieces) {
            piece.draw(g, cellSize);
        }
    }
}
