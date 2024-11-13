package network;

public class GameRoom {
    private final String id;
    private final ClientHandler player1;
    private final ClientHandler player2;

    public GameRoom(String id, ClientHandler player1, ClientHandler player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void broadcastMove(String move, ClientHandler sender) {
        ClientHandler recipient = (sender == player1) ? player2 : player1;
        if (recipient != null) {
            recipient.sendMessage("MOVE " + move); // Gửi nước đi cho đối thủ
        }
    }
    public String getId() {
        return id;
    }
}
