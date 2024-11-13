package network;

public class GameRoom {
    private final String id;
    private final Server.ClientHandler player1;
    private final Server.ClientHandler player2;

    public GameRoom(String id, Server.ClientHandler player1, Server.ClientHandler player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void broadcastMove(String move, Server.ClientHandler sender) {
        Server.ClientHandler recipient = (sender == player1) ? player2 : player1;
        recipient.sendMessage("MOVE " + move);
    }
    public String getId() {
        return id;
    }
}
