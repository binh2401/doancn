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
    public class ClientHandler implements Runnable{
        private Socket socket;
        private Server server;
        private PrintWriter out;
        private ClientHandler opponent;
        private BufferedReader in;
        private static List<PrintWriter> clientWriters = new ArrayList<>();
        public ClientHandler(Socket socket, Server server) {
            this.socket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received message: " + message);
                    if (message.equals("FIND_OPPONENT")) {
                        server.findOpponent(this); // Tìm đối thủ thông qua server
                    } else if (message.startsWith("MOVE")) {
                        System.out.println("Server received move: " + message);
                        if (opponent != null) {
                            opponent.sendMessage(message); // Chuyển nước đi cho đối thủ
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  server.removeClient(this); // Xóa client khỏi server khi ngắt kết nối
            }
        }


        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
                out.flush();
            }
        }

        public void setOpponent(ClientHandler opponent) {
            this.opponent = opponent;
        }

        public ClientHandler getOpponent() {
            return opponent;
        }
    }
}
