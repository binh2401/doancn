package AI;

import quanco.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import sounds.SoundPlayer;

public class Board extends JPanel {
    private final int boardWidth = 9;  // Số cột
    private final int boardHeight = 10; // Số hàng
    private final int cellSize = 70;    // Kích thước của mỗi ô
    private List<Piece> pieces;          // Danh sách các quân cờ
    private Image boardImage;            // Hình ảnh bàn cờ
    private Piece selectedPiece;          // Quân cờ đang được chọn
    private int mouseX, mouseY;          // Vị trí chuột khi kéo
    private boolean isRedTurn = true;    // Biến xác định lượt
    private Timer timer;                  // Bộ đếm thời gian
    private int timeLeft = 60;            // Thời gian còn lại
    private JLabel timerLabel;            // Nhãn hiển thị thời gian
    private List<MovePair> moveHistoryPairs;
    private Move lastRedMove = null;
    private Move lastBlackMove = null;
    private boolean isAIEnabled;

    public Board(boolean isAIEnabled) {
        this.isAIEnabled = isAIEnabled;
        setPreferredSize(new Dimension(boardWidth * cellSize, boardHeight * cellSize));
        pieces = new ArrayList<>();
        moveHistoryPairs = new ArrayList<>(); // Khởi tạo danh sách lịch sử nước đi

        // Thêm quân Tướng Đỏ và Đen vào danh sách
        pieces.add(new King(4, 9, true, pieces));  // Tướng Đỏ ở vị trí (4, 9)
        pieces.add(new King(4, 0, false, pieces)); // Tướng Đen ở vị trí (4, 0)

        // Thêm các quân cờ khác...
        pieces.add(new chot(0, 6, true, pieces));
        pieces.add(new chot(0, 3, false, pieces));
        pieces.add(new chot(2, 6, true, pieces));
        pieces.add(new chot(2, 3, false, pieces));
        pieces.add(new chot(4, 6, true, pieces));
        pieces.add(new chot(4, 3, false, pieces));
        pieces.add(new chot(6, 6, true, pieces));
        pieces.add(new chot(6, 3, false, pieces));
        pieces.add(new chot(8, 6, true, pieces));
        pieces.add(new chot(8, 3, false, pieces));

        // Thêm quân xe, mã, tướng, si, pháo...
        pieces.add(new xe(0, 9, true, pieces));
        pieces.add(new xe(0, 0, false, pieces));
        pieces.add(new xe(8, 9, true, pieces));
        pieces.add(new xe(8, 0, false, pieces));
        pieces.add(new ma(1, 9, true, pieces));
        pieces.add(new ma(1, 0, false, pieces));
        pieces.add(new ma(7, 9, true, pieces));
        pieces.add(new ma(7, 0, false, pieces));
        pieces.add(new tuong(2, 9, true, pieces));
        pieces.add(new tuong(2, 0, false, pieces));
        pieces.add(new tuong(6, 9, true, pieces));
        pieces.add(new tuong(6, 0, false, pieces));
        pieces.add(new si(3, 9, true, pieces));
        pieces.add(new si(3, 0, false, pieces));
        pieces.add(new si(5, 9, true, pieces));
        pieces.add(new si(5, 0, false, pieces));
        pieces.add(new phao(1, 7, true, pieces));
        pieces.add(new phao(1, 2, false, pieces));
        pieces.add(new phao(7, 7, true, pieces));
        pieces.add(new phao(7, 2, false, pieces));

        // Khởi tạo nhãn thời gian
        timerLabel = new JLabel("Time left: " + timeLeft);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        add(timerLabel, BorderLayout.NORTH); // Thêm nhãn vào JPanel

        // Khởi động bộ đếm thời gian
        startTimer();

        // Thêm các sự kiện chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y && piece.isRed() == isRedTurn) {
                        selectedPiece = piece; // Lưu quân cờ được chọn
                        break;
                    }
                }
                mouseX = e.getX();
                mouseY = e.getY();
                repaint(); // Vẽ lại bảng cờ
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedPiece != null) {
                    int newX = e.getX() / cellSize;
                    int newY = e.getY() / cellSize;

                    if (isRedTurn) { // Nếu lượt là của người chơi đỏ
                        // Kiểm tra xem di chuyển có hợp lệ không
                        if (selectedPiece.isValidMove(newX, newY) && newY >= 0 && newY < boardHeight) {
                            Piece targetPiece = getPieceAt(newX, newY); // Kiểm tra quân ở vị trí mới

                            if (targetPiece == null || targetPiece.isRed() != selectedPiece.isRed()) {
                                // Nếu không có quân cờ hoặc có quân địch
                                if (targetPiece != null) {
                                    pieces.remove(targetPiece); // Loại bỏ quân địch
                                }

                                // Lưu vị trí cũ để khôi phục nếu cần
                                int originalX = selectedPiece.getX();
                                int originalY = selectedPiece.getY();

                                // Thực hiện di chuyển
                                selectedPiece.setPosition(newX, newY);

                                // Kiểm tra nếu quân cờ vẫn bị chiếu sau nước đi này
                                if (isCheck(isRedTurn)) {
                                    // Nếu nước đi không thoát khỏi chiếu, hoàn tác di chuyển
                                    selectedPiece.setPosition(originalX, originalY);
                                    if (targetPiece != null) {
                                        pieces.add(targetPiece); // Khôi phục quân cờ địch
                                    }
                                } else {
                                    // Nước đi hợp lệ
                                    Move move = new Move(selectedPiece, originalX, originalY, newX, newY, targetPiece);
                                    if (isRedTurn) {
                                        lastRedMove = move;
                                    } else {
                                        lastBlackMove = move;
                                        if (lastRedMove != null) {
                                            moveHistoryPairs.add(new MovePair(lastRedMove, lastBlackMove));
                                            lastRedMove = null;
                                            lastBlackMove = null;
                                        }
                                    }

                                    // Kiểm tra chiếu sau khi di chuyển
                                    if (isCheck(!isRedTurn)) {
                                        JOptionPane.showMessageDialog(Board.this,
                                                (isRedTurn ? "Đen" : "Đỏ") + " bị chiếu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                                    }

                                    // Kiểm tra chiếu tướng
                                    if (isCheckmate(!isRedTurn)) {
                                        JOptionPane.showMessageDialog(Board.this,
                                                (isRedTurn ? "Đen" : "Đỏ") + " đã thua!", "Game Over", JOptionPane.WARNING_MESSAGE);
                                        System.exit(0); // Kết thúc trò chơi
                                    }

                                    isRedTurn = false; // Đổi lượt cho AI
                                    timeLeft = 60; // Reset thời gian
                                    if (isAIEnabled) {
                                        // Nếu lượt của AI, thực hiện nước đi của AI
                                        AIPlayer aiPlayer = new AIPlayer();
                                        Move aiMove = aiPlayer.getBestMove(Board.this, isRedTurn);
                                        if (aiMove != null) {
                                            makeMove(aiMove);
                                        }
                                    }
                                    // Đổi lượt trở lại cho người chơi
                                    isRedTurn = true;

                                    // Vẽ lại bàn cờ sau khi di chuyển
                                    repaint(); // Gọi repaint để cập nhật bàn cờ
                                }
                            }
                        }


                    }

                    // Phát âm thanh khi di chuyển quân cờ
                    SoundPlayer soundPlayer = new SoundPlayer();
                    if (selectedPiece.isRed()) {
                        soundPlayer.playSound("/sounds/move.wav"); // Âm thanh cho quân đỏ
                    } else {
                        soundPlayer.playSound("/sounds/move2.wav"); // Âm thanh cho quân đen
                    }

                    selectedPiece = null; // Đặt lại quân cờ được chọn
                    repaint(); // Vẽ lại bảng cờ
                }
            }

        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPiece != null) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint(); // Vẽ lại để quân cờ di chuyển theo chuột
                }
            }
        });

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
    // Phương thức hoàn tác nước đi cuối cùng
    // Phương thức hoàn tác nước đi cuối cùng
    public boolean undoLastMovePair() {
        if (moveHistoryPairs.isEmpty()) {
            return false;
        }

        MovePair lastPair = moveHistoryPairs.remove(moveHistoryPairs.size() - 1);

        if (lastPair.getRedMove() != null) {
            lastPair.getRedMove().getPiece().setPosition(lastPair.getRedMove().getOldX(), lastPair.getRedMove().getOldY());
            if (lastPair.getRedMove().getCapturedPiece() != null) {
                pieces.add(lastPair.getRedMove().getCapturedPiece());
            }
        }

        if (lastPair.getBlackMove() != null) {
            lastPair.getBlackMove().getPiece().setPosition(lastPair.getBlackMove().getOldX(), lastPair.getBlackMove().getOldY());
            if (lastPair.getBlackMove().getCapturedPiece() != null) {
                pieces.add(lastPair.getBlackMove().getCapturedPiece());
            }
        }

        isRedTurn = !isRedTurn;
        repaint();
        return true;
    }

    public List<Move> getAllPossibleMoves(boolean isRed) {
        List<Move> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.isRed() == isRed) {
                for (int[] validMove : piece.getValidMoves()) {
                    moves.add(new Move(piece, piece.getX(), piece.getY(), validMove[0], validMove[1], getPieceAt(validMove[0], validMove[1])));
                }
            }
        }
        return moves;
    }

    public void makeMove(Move move) {
        Piece targetPiece = getPieceAt(move.getNewX(), move.getNewY());
        if (targetPiece != null) {
            pieces.remove(targetPiece); // Xóa quân cờ bị ăn
        }
        Piece movedPiece = move.getPiece(); // Lấy quân cờ cần di chuyển
        if (movedPiece != null) {
            movedPiece.setPosition(move.getNewX(), move.getNewY()); // Di chuyển quân cờ
        } else {
            System.out.println("Quân cờ không tồn tại.");
        }
        repaint(); // Vẽ lại bàn cờ
    }
    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                timerLabel.setText("Time left: " + timeLeft);
            } else {
                // Nếu thời gian hết, thông báo và kết thúc trò chơi
                JOptionPane.showMessageDialog(this, (isRedTurn ? "Đỏ" : "Đen") + " đã hết thời gian!", "Game Over", JOptionPane.WARNING_MESSAGE);
                System.exit(0); // Thoát trò chơi
            }
        });
        timer.start();
    }

    // Phương thức vẽ bàn cờ
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ hình ảnh bàn cờ
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, boardWidth * cellSize, boardHeight * cellSize, this);
        }

        // Nếu có quân cờ được chọn, vẽ dấu chấm vàng và các nước đi hợp lệ
        if (selectedPiece != null) {
            // Vẽ dấu chấm vàng cho quân cờ được chọn
            g.setColor(Color.YELLOW);
            g.fillOval(mouseX - cellSize / 4, mouseY - cellSize / 4, cellSize / 2, cellSize / 2);

            // Vẽ các nước đi hợp lệ
            List<int[]> validMoves = selectedPiece.getValidMoves();
            g.setColor(new Color(0, 255, 0, 128)); // Màu xanh với độ trong suốt
            for (int[] move : validMoves) {
                int validX = move[0];
                int validY = move[1];
                g.fillRect(validX * cellSize + 10, validY * cellSize + 10, cellSize - 20, cellSize - 20); // Vẽ ô cho nước đi hợp lệ
            }
        }

        // Vẽ các quân cờ
        for (Piece piece : pieces) {
            piece.draw(g, cellSize);
        }
    }

    private Piece getPieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        return null;
    }

    // Hàm kiểm tra chiếu
    private boolean isCheck(boolean isRed) {
        Piece king = null;
        for (Piece piece : pieces) {
            if (piece instanceof King && piece.isRed() == isRed) {
                king = piece;
                break;
            }
        }
        if (king == null) return true;

        for (Piece piece : pieces) {
            if (piece.isRed() != isRed && piece.isValidMove(king.getX(), king.getY())) {
                return true; // Quân địch có thể ăn Tướng
            }
        }
        return false;
    }

    // Hàm kiểm tra chiếu tướng
    private boolean isCheckmate(boolean isRed) {
        if (!isCheck(isRed)) return false;

        for (Piece piece : pieces) {
            if (piece.isRed() == isRed) {
                for (int[] move : piece.getValidMoves()) {
                    int originalX = piece.getX();
                    int originalY = piece.getY();

                    piece.setPosition(move[0], move[1]);

                    if (!isCheck(isRed)) {
                        piece.setPosition(originalX, originalY);
                        return false;
                    }
                    piece.setPosition(originalX, originalY);
                }
            }
        }
        return true;
    }
    // Lớp Move để lưu trữ thông tin của mỗi nước đi


    class MovePair {
        private final Move redMove;
        private final Move blackMove;

        public MovePair(Move redMove, Move blackMove) {
            this.redMove = redMove;
            this.blackMove = blackMove;
        }

        public Move getRedMove() {
            return redMove;
        }

        public Move getBlackMove() {
            return blackMove;
        }
    }
    public boolean isGameOver() {
        // Kiểm tra nếu một trong hai tướng đã bị bắt
        for (Piece piece : pieces) {
            if (piece instanceof King && piece.isCaptured()) {
                return true; // Trò chơi kết thúc vì một bên bị mất tướng
            }
        }
        // Kiểm tra nếu không còn nước đi hợp lệ cho người chơi hiện tại

        return false; // Trò chơi chưa kết thúc
    }

    public void undoMove(Move move) {
        if (move != null) {
            Piece movedPiece = move.getMovedPiece();
            if (movedPiece != null) {
                movedPiece.setPosition(move.getStartPosition());
                if (move.getCapturedPiece() != null) {
                    pieces.add(move.getCapturedPiece());
                }
                // Cập nhật lại các trạng thái khác nếu cần
            } else {
                System.out.println("movedPiece is null. Check the logic when creating Move.");
            }
        }

    }

    public List<Piece> getPieces() {
        // Trả về danh sách các quân cờ hiện tại
        return new ArrayList<>(pieces);
    }
    public List<Move> getAllValidMoves() {
        List<Move> validMoves = new ArrayList<>();

        for (Piece piece : pieces) {
            if (!piece.isCaptured()) { // Kiểm tra xem quân cờ có đang trên bàn cờ hay không
                List<Move> moves = piece.getValidMoves(this); // Giả sử Piece có phương thức getValidMoves
                validMoves.addAll(moves);
            }
        }

        return validMoves;
    }

}