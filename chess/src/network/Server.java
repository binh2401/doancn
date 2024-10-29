package network;

import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    private static final int PORT = 12345;
    private List<ClientHandler> clients = new ArrayList<>();

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);

                new Thread(clientHandler).start();

                // Nếu đủ 2 client, thông báo sẵn sàng bắt đầu trò chơi
                if (clients.size() == 2) {
                    for (ClientHandler client : clients) {
                        client.sendMessage("READY_TO_START");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMove(String move, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(move);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
