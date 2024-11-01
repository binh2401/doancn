package network;

import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> waitingClients = new ArrayList<>();


    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server đang chạy trên cổng " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client đã kết nối.");

                ClientHandler clientHandler = new ClientHandler(socket, this);
                new Thread(clientHandler).start(); // Chạy ClientHandler trong một luồng mới
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void findOpponent(ClientHandler clientHandler) {
        if (waitingClients.isEmpty()) {
            waitingClients.add(clientHandler);
            clientHandler.sendMessage("Vui lòng chờ đối thủ...");
        } else {
            ClientHandler opponent = waitingClients.remove(0);
            clientHandler.setOpponent(opponent);
            opponent.setOpponent(clientHandler);

            clientHandler.sendMessage("GAME_START");
            opponent.sendMessage("GAME_START");
        }
    }


    public static void main(String[] args) {
        int port = 12345; // Bạn có thể thay đổi cổng nếu cần
        Server server = new Server(port);
        server.start();
    }
}
