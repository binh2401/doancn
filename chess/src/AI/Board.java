package AI;

import giaodien.StartWindow;
import network.Client;
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
    private Image backgroundImage;
    private Piece selectedPiece;          // Quân cờ đang được chọn
    private int mouseX, mouseY;          // Vị trí chuột khi kéo
    public boolean isRedTurn = true;    // Biến xác định lượt
    private Timer timer;                  // Bộ đếm thời gian
    private int timeLeft = 60;            // Thời gian còn lại
    private JLabel timerLabel;            // Nhãn hiển thị thời gian
    private List<MovePair> moveHistoryPairs;
    private Move lastRedMove = null;
    private Move lastBlackMove = null;
    private boolean isAIEnabled;
    private Piece[][] board;
    private boolean gameOver = false;
    private Client client;

    private String roomId;
    public Board(boolean isAIEnabled,String difficulty,Client client,String roomId) {
        this.client = client;
        this.roomId = roomId;
        this.isAIEnabled = isAIEnabled;
        board = new Piece[10][9];
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
                // Tính toán vị trí của chuột khi nhấn, bao gồm việc căn giữa bàn cờ
                int x = (e.getX() - (getWidth() - boardWidth * cellSize) / 2) / cellSize;
                int y = (e.getY() - (getHeight() - boardHeight * cellSize) / 2) / cellSize;

                // Kiểm tra nếu chuột nhấn vào một ô hợp lệ trong bàn cờ (trong phạm vi 9x10)
                if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
                    // Kiểm tra nếu quân cờ ở vị trí nhấn chuột và nếu đó là lượt của người chơi
                    for (Piece piece : pieces) {
                        if (piece.getX() == x && piece.getY() == y && piece.isRed() == isRedTurn) {
                            selectedPiece = piece; // Lưu quân cờ được chọn
                            break;
                        }
                    }
                }

                // Lưu vị trí chuột để dùng khi di chuyển quân
                mouseX = e.getX();
                mouseY = e.getY();
                repaint(); // Vẽ lại bàn cờ
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedPiece != null) {
                    // Tính toán vị trí ô mới khi thả chuột, bao gồm việc căn giữa bàn cờ
                    int newX = (e.getX() - (getWidth() - boardWidth * cellSize) / 2) / cellSize;
                    int newY = (e.getY() - (getHeight() - boardHeight * cellSize) / 2) / cellSize;

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


                                sendMoveData(originalX, originalY, newX, newY);
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
                                        int option = JOptionPane.showOptionDialog(Board.this,
                                                (isRedTurn ? "Đen" : "Đỏ") + " đã thua! Bạn muốn làm gì tiếp theo?",
                                                "Game Over",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE,
                                                null,
                                                new String[]{"Chơi Tiếp", "Về Trang Chính"}, // Tên các nút
                                                "Chơi Tiếp"); // Nút mặc định

                                        if (option == JOptionPane.YES_OPTION) {
                                            // Chơi tiếp: Reset lại trò chơi
                                            resetGame();
                                        } else if (option == JOptionPane.NO_OPTION) {
                                            stopBoard();
                                            // Về trang chính: Đóng cửa sổ hiện tại và mở StartWindow
                                            JFrame startWindow = new StartWindow(client); // Giả sử StartWindow là giao diện chính
                                            startWindow.setVisible(true);
                                            SwingUtilities.getWindowAncestor(Board.this).dispose(); // Đóng cửa sổ hiện tại
                                        }
                                    }

                                    isRedTurn = false; // Đổi lượt cho AI
                                    timeLeft = 60; // Reset thời gian
                                    if (isAIEnabled) {
                                        AIPlayer aiPlayer = new AIPlayer();
                                        aiPlayer.setDifficultyLevel(difficulty);
                                        long timeLimit = 1000;  // 1 second
                                        Move aiMove = aiPlayer.getBestMove(Board.this, isRedTurn, timeLimit);

                                        if (aiMove != null) {
                                            makeMove(aiMove); // Thực hiện nước đi của AI
                                            lastBlackMove = aiMove; // Lưu lại nước đi của AI

                                            // Thêm vào lịch sử nước đi nếu cả người và AI đều có nước đi
                                            if (lastRedMove != null) {
                                                moveHistoryPairs.add(new MovePair(lastRedMove, lastBlackMove));
                                                lastRedMove = null;
                                                lastBlackMove = null;
                                            }
                                        }
                                        if (isCheck(!isRedTurn)) {
                                            JOptionPane.showMessageDialog(Board.this,
                                                    (isRedTurn ? "Đen" : "Đỏ") + " bị chiếu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                                        }

// Kiểm tra chiếu tướng
                                        if (isCheckmate(!isRedTurn)) {
                                            int option = JOptionPane.showOptionDialog(Board.this,
                                                    (isRedTurn ? "Đen" : "Đỏ") + " đã thua! Bạn muốn làm gì?", // Nội dung thông báo
                                                    "Game Over", // Tiêu đề
                                                    JOptionPane.DEFAULT_OPTION, // Loại hộp thoại
                                                    JOptionPane.WARNING_MESSAGE, // Icon cảnh báo
                                                    null, // Icon tùy chỉnh (để null dùng mặc định)
                                                    new Object[]{"Chơi tiếp", "Quay về StartWindow"}, // Các lựa chọn nút
                                                    "Chơi tiếp" // Nút mặc định
                                            );

                                            // Xử lý lựa chọn
                                            if (option == 0) {
                                                // Người chơi chọn "Chơi tiếp"
                                                resetGame(); // Hàm để reset bàn cờ và chơi lại
                                            } else if (option == 1) {
                                                // Người chơi chọn "Quay về StartWindow"
                                                stopBoard();

                                                // Về trang chính: Đóng cửa sổ hiện tại và mở StartWindow
                                                JFrame startWindow = new StartWindow(client); // Truyền tham chiếu client nếu cần
                                                startWindow.setVisible(true);
                                                SwingUtilities.getWindowAncestor(Board.this).dispose();
                                            }
                                        }
                                    }
                                    // Đổi lượt trở lại cho người chơi
                                    isRedTurn = true;
                                    timeLeft = 60;
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
            boardImage = ImageIO.read(getClass().getResourceAsStream("/img/board.gif"));
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/img/HinhNen/backgroundboard.jpg"));// Sử dụng "/" để chỉ đường dẫn từ thư mục gốc
            if (boardImage == null) {
                System.out.println("Hình ảnh bàn cờ không thể tải!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendMoveData(int originalX, int originalY, int newX, int newY) {
        if (client != null || roomId!= null) {
            String roomIdFromClient = getRoomId(roomId);
            if (roomIdFromClient != null) {
                String moveData = "MOVE " + originalX + " " + originalY + " " + newX + " " + newY;
                System.out.println("Move Data: " + moveData);
                client.sendMessage(moveData, roomIdFromClient); // Gửi thông điệp và roomId
            } else {
                System.err.println("Room ID not found for the client.");
            }
        } else {
            System.err.println("Client not connected.");
        }
    }
    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    pieces.add(board[row][col]);
                }
            }
        }
        return pieces;
    }

    // Phương thức hoàn tác nước đi cuối cùng
    public boolean undoLastMovePair() {
        if (moveHistoryPairs.isEmpty()) {
            return false; // Không có nước đi nào để hoàn tác
        }

        // Lấy cặp nước đi cuối cùng
        MovePair lastPair = moveHistoryPairs.remove(moveHistoryPairs.size() - 1);

        // Hoàn tác nước đi của Đỏ
        if (lastPair.getRedMove() != null) {
            Move redMove = lastPair.getRedMove();
            redMove.getPiece().setPosition(redMove.getOldX(), redMove.getOldY());
            if (redMove.getCapturedPiece() != null) {
                pieces.add(redMove.getCapturedPiece()); // Khôi phục quân cờ bị bắt
            }
        }

        // Hoàn tác nước đi của Đen
        if (lastPair.getBlackMove() != null) {
            Move blackMove = lastPair.getBlackMove();
            blackMove.getPiece().setPosition(blackMove.getOldX(), blackMove.getOldY());
            if (blackMove.getCapturedPiece() != null) {
                pieces.add(blackMove.getCapturedPiece()); // Khôi phục quân cờ bị bắt
            }
        }

        // Đặt lượt chơi về Đỏ
        isRedTurn = true;

        // Vẽ lại bàn cờ sau khi hoàn tác
        repaint();

        return true; // Hoàn tác thành công
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
                // Nếu thời gian hết, hiển thị hộp thoại với 2 tùy chọn
                int option = JOptionPane.showOptionDialog(this,
                        (isRedTurn ? "Đỏ" : "Đen") + " đã hết thời gian! Bạn muốn làm gì tiếp theo?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Chơi Tiếp", "Về Trang Chính"}, // Các nút tùy chọn
                        "Chơi Tiếp"); // Nút mặc định

                if (option == JOptionPane.YES_OPTION) {
                    // Nếu người chơi chọn "Chơi Tiếp", bắt đầu lại thời gian hoặc tiếp tục trò chơi
                    timeLeft = 60; // Reset lại thời gian (hoặc thiết lập thời gian mới)
                    timerLabel.setText("Time left: " + timeLeft);
                    startTimer(); // Bắt đầu lại timer
                } else if (option == JOptionPane.NO_OPTION) {
                    if (timer != null) {
                        timer.stop(); // Dừng Timer
                    }
                    // Nếu người chơi chọn "Về Trang Chính", đóng cửa sổ hiện tại và mở StartWindow
                    JFrame startWindow = new StartWindow(client); // Giả sử bạn đã có lớp StartWindow mở giao diện chính
                    startWindow.setVisible(true);
                    SwingUtilities.getWindowAncestor(this).dispose(); // Đóng cửa sổ hiện tại
                }
            }
        });
        timer.start();
    }

    // Phương thức vẽ bàn cờ
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Tính toán vị trí vẽ bàn cờ sao cho nó nằm chính giữa màn hình
        int x = (getWidth() - boardWidth * cellSize) / 2;
        int y = (getHeight() - boardHeight * cellSize) / 2;

        // Vẽ hình nền của bàn cờ
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // Vẽ hình nền nền cho bàn cờ
        }

        // Vẽ hình ảnh bàn cờ lên chính giữa màn hình
        if (boardImage != null) {
            g.drawImage(boardImage, x, y, boardWidth * cellSize, boardHeight * cellSize, null); // Vẽ bàn cờ vào vị trí căn giữa
        }

        // Nếu có quân cờ được chọn, vẽ dấu chấm vàng và các nước đi hợp lệ
        if (selectedPiece != null) {
            // Vẽ các nước đi hợp lệ
            List<int[]> validMoves = selectedPiece.getValidMoves();
            g.setColor(new Color(0, 255, 0, 128)); // Màu xanh với độ trong suốt
            for (int[] move : validMoves) {
                int validX = move[0];
                int validY = move[1];
                g.fillRect(validX * cellSize + x + 10, validY * cellSize + y + 10, cellSize - 20, cellSize - 20); // Vẽ ô cho nước đi hợp lệ
            }
        }
        // Vẽ các quân cờ
        if (pieces != null) {
            for (Piece piece : pieces) {
                if (piece != null) {
                    piece.draw(g, cellSize, x, y); // Gọi phương thức draw của quân cờ
                }
            }
        }
        // Vẽ các quân cờ lên chính giữa màn hình
        for (Piece piece : pieces) {
            piece.draw(g, cellSize, x, y); // Đảm bảo vẽ quân cờ vào vị trí chính xác với offset
        }

    }


    public Piece getPieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        return null;
    }

    // Hàm kiểm tra chiếu
    public boolean isCheck(boolean isRed) {
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
    public boolean isCheckmate(boolean isRed) {
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
        return pieces;
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
    public void resetGame() {
        pieces.clear();
        moveHistoryPairs.clear();
        lastRedMove = null;
        lastBlackMove = null;
        isRedTurn = true;
        timeLeft = 60;

        // Thêm lại các quân cờ vào bàn
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

        // Reset lại thời gian và nhãn
        timerLabel.setText("Time left: " + timeLeft);

        repaint(); // Vẽ lại bàn cờ
    }
    // Hàm xử lý sự kiện đầu hàng
    public void surrender() {
        // Kiểm tra ai là người thua cuộc và thông báo
        String loser = isRedTurn ? "Đỏ" : "Đen";


        // Hiển thị hộp thoại với hai tùy chọn: Chơi Tiếp hoặc Về Trang Chính
        int option = JOptionPane.showOptionDialog(this,
                loser + " đã đầu hàng. "  + " thắng! Bạn muốn làm gì tiếp theo?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Chơi Tiếp", "Về Trang Chính"}, // Tên các nút
                "Chơi Tiếp"); // Nút mặc định

        if (option == JOptionPane.YES_OPTION) {
            // Chơi tiếp: Reset lại trò chơi
            resetGame();
        } else if (option == JOptionPane.NO_OPTION) {
            // Dừng tất cả hoạt động của Board trước khi quay lại StartWindow
            stopBoard();

            // Về trang chính: Đóng cửa sổ hiện tại và mở StartWindow
            JFrame startWindow = new StartWindow(client); // Truyền tham chiếu client nếu cần
            startWindow.setVisible(true);
            SwingUtilities.getWindowAncestor(this).dispose(); // Đóng cửa sổ hiện tại
        }
    }

    // Phương thức dừng các hoạt động của Board
    public void stopBoard() {
        // Hủy bỏ các luồng, bộ đếm thời gian hoặc bất kỳ tác vụ nào đang chạy
        if (timer != null) {
            timer.stop(); // Dừng Timer
        }

        // Dọn dẹp các tài nguyên khác nếu cần
        // ...
    }


    // Phương thức để lấy ID phòng
    public String getRoomId(String roomId) {
        return this.roomId;
    }
    public void clearPieceAt(int x, int y) {
        pieces = new ArrayList<>();
        pieces.removeIf(piece -> piece.getX() == x && piece.getY() == y);
    }
    public void resetTimer() {
        timeLeft = 60; // Thiết lập lại thời gian về 60 giây
        timerLabel.setText("Time left: " + timeLeft); // Cập nhật giao diện
        if (timer != null && timer.isRunning()) {
            timer.stop();  // Dừng bộ đếm thời gian hiện tại
        }
        startTimer();  // Khởi động lại bộ đếm thời gian mới từ 60 giây
    }

}