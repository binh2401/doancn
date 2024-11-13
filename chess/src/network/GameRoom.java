package network;

public class GameRoom {
    private final String id;
    private final ClientHandler player1;
    private final ClientHandler player2;
    private boolean isPlayer1Turn; // Biến theo dõi lượt đi của player1

    public GameRoom(String id, ClientHandler player1, ClientHandler player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.isPlayer1Turn = true; // Player 1 đi trước
    }

    // Phương thức gửi nước đi cho đối thủ
    public void broadcastMove(String move, ClientHandler sender) {
        // Kiểm tra lượt đi có hợp lệ không
        if ((sender == player1 && isPlayer1Turn) || (sender == player2 && !isPlayer1Turn)) {
            ClientHandler recipient = (sender == player1) ? player2 : player1;
            if (recipient != null) {
                recipient.sendMessage("MOVE " + move); // Gửi nước đi cho đối thủ
                System.out.println("Broadcasting move to opponent: " + move);  // Log việc gửi nước đi
            }
            // Đổi lượt sau khi phát sóng nước đi
            switchTurn();
        } else {
            // Nếu người chơi đi sai lượt
            sender.sendMessage("WAIT_FOR_YOUR_TURN");
        }
    }

    // Phương thức bắt đầu game và thông báo màu quân
    public void startGame() {
        // Gửi thông báo bắt đầu game và phân chia quân
        player1.sendMessage("GAME_START RED");
        player2.sendMessage("GAME_START BLACK");
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
}
