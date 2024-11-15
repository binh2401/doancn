package network;

public class GameRoom {
    private final String id;
    private  ClientHandler player1;
    private  ClientHandler player2;
    private boolean isPlayer1Turn; // Biến theo dõi lượt đi của player1
    private String boardState; // Trạng thái bàn cờ ban đầu
    private boolean gameOver; // Biến theo dõi tình trạng trò chơi (kết thúc hay chưa)

    public GameRoom(String id, ClientHandler player1, ClientHandler player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.isPlayer1Turn = true; // Player 1 đi trước
        this.boardState = initializeBoard(); // Khởi tạo trạng thái bàn cờ ban đầu
        this.gameOver = false; // Ban đầu trò chơi chưa kết thúc
    }

    // Phương thức để khởi tạo trạng thái bàn cờ
    private String initializeBoard() {
        // Khởi tạo trạng thái bàn cờ mặc định dưới dạng chuỗi (ví dụ: vị trí các quân cờ)
        return "InitialBoardState"; // Thay bằng trạng thái bàn cờ thật
    }

    // Phương thức gửi nước đi cho đối thủ
    public void broadcastMove(String move, ClientHandler sender) {
        if (gameOver) {
            sender.sendMessage("GAME_OVER Game has already ended.");  // Nếu trò chơi đã kết thúc, không thể thực hiện nước đi
            return;
        }

        // Kiểm tra lượt đi có hợp lệ không
        if ((sender == player1 && isPlayer1Turn) || (sender == player2 && !isPlayer1Turn)) {
            ClientHandler recipient = (sender == player1) ? player2 : player1;
            if (recipient != null) {
                recipient.sendMessage("MOVE " + move); // Gửi nước đi cho đối thủ
                System.out.println("Broadcasting move to opponent: " + move);  // Log việc gửi nước đi
                updateBoardState(move); // Cập nhật trạng thái bàn cờ sau mỗi nước đi
            }
            // Kiểm tra kết thúc trò chơi
            checkGameStatus();
            // Đổi lượt sau khi phát sóng nước đi
            switchTurn();
        } else {
            // Nếu người chơi đi sai lượt
            sender.sendMessage("WAIT_FOR_YOUR_TURN");
        }
    }

    // Phương thức cập nhật trạng thái bàn cờ
    private void updateBoardState(String move) {
        // Cập nhật trạng thái bàn cờ (bạn cần viết logic cụ thể để cập nhật trạng thái bàn cờ)
        boardState = "UpdatedBoardState";  // Thay đổi trạng thái bàn cờ sau mỗi nước đi
    }

    // Phương thức kiểm tra tình trạng trò chơi (thắng, thua, hòa)
    private void checkGameStatus() {
        // Kiểm tra điều kiện thắng thua hoặc hòa (cần logic cụ thể về game rules)
        if (isCheckmate(true)) { // Kiểm tra nếu player1 thua (vì player1 là quân đỏ)
            gameOver = true;
            player1.sendMessage("GAME_OVER YOU_LOSE");
            player2.sendMessage("GAME_OVER YOU_WIN");
            System.out.println("Game over. Player1 loses.");
        } else if (isCheckmate(false)) { // Kiểm tra nếu player2 thua (vì player2 là quân đen)
            gameOver = true;
            player1.sendMessage("GAME_OVER YOU_WIN");
            player2.sendMessage("GAME_OVER YOU_LOSE");
            System.out.println("Game over. Player2 loses.");
        } else if (isDrawCondition()) {
            gameOver = true;
            player1.sendMessage("GAME_OVER DRAW");
            player2.sendMessage("GAME_OVER DRAW");
            System.out.println("Game over. It's a draw.");
        }
    }

    // Phương thức kiểm tra điều kiện hòa (ví dụ: không còn nước đi hợp lệ)
    private boolean isDrawCondition() {
        // Kiểm tra điều kiện hòa (tùy thuộc vào luật chơi của bạn)
        return false; // Giả sử không có điều kiện hòa
    }

    // Phương thức kiểm tra tình trạng chiếu tướng (thua) cho người chơi
    private boolean isCheckmate(boolean isRed) {
        // Kiểm tra điều kiện chiếu tướng cho quân đỏ (isRed = true) hoặc quân đen (isRed = false)
        return false; // Đây là ví dụ đơn giản, bạn cần logic kiểm tra chiếu tướng tại đây
    }

    // Phương thức bắt đầu game và gửi trạng thái bàn cờ ban đầu
    public void startGame() {
        if (gameOver) {
            System.out.println("Game has already ended. Cannot start a new game.");
            return;
        }

        // Gửi thông báo bắt đầu game, phân chia quân và gửi trạng thái bàn cờ ban đầu
        player1.sendMessage("GAME_START RED " + boardState);
        player2.sendMessage("GAME_START BLACK " + boardState);
    }

    // Kiểm tra xem có phải lượt của player1 không
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    // Chuyển lượt giữa 2 người chơi
    public void switchTurn() {
        isPlayer1Turn = !isPlayer1Turn; // Đổi lượt cho người chơi
    }

    // Trả về ID của phòng
    public String getId() {
        return id;
    }

    // Phương thức getter cho player1
    public ClientHandler getPlayer1() {
        return player1;
    }

    // Phương thức getter cho player2
    public ClientHandler getPlayer2() {
        return player2;
    }

    // Trả về trạng thái bàn cờ
    public String getBoardState() {
        return boardState;
    }
    public void removePlayer(ClientHandler player) {
        if (player == player1) {
            player1.sendMessage("You have been removed from the game room.");
            player1 = null; // Gán player1 thành null khi người chơi rời khỏi
        } else if (player == player2) {
            player2.sendMessage("You have been removed from the game room.");
            player2 = null; // Gán player2 thành null khi người chơi rời khỏi
        }

        // Nếu cả hai người chơi đều đã rời, kết thúc trò chơi
        if (player1 == null && player2 == null) {
            gameOver = true; // Đặt trò chơi kết thúc nếu không còn người chơi
            System.out.println("Game over. No players left.");
        }
    }
    public void broadcastMessage(String message) {
        if (player1 != null) {
            player1.sendMessage(message);
        }
        if (player2 != null) {
            player2.sendMessage(message);
        }
        System.out.println("Broadcasting message to players: " + message); // Log việc gửi thông báo
    }
}
