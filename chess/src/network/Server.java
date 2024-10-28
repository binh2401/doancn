package network;

import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    private static final int PORT = 12345; // Cổng server
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static boolean isRedTurn = true; // Đổi lượt chơi

    public static void startServer() {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start(); // Mỗi kết nối mới tạo một thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message); // Gửi thông điệp đến tất cả client
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                synchronized (clientWriters) {
                    clientWriters.add(out); // Thêm writer vào danh sách client
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    // Kiểm tra lượt chơi
                    if ((isRedTurn && message.startsWith("RED")) || (!isRedTurn && message.startsWith("BLACK"))) {
                        broadcast(message); // Gửi thông điệp đến tất cả client
                        isRedTurn = !isRedTurn; // Đổi lượt chơi
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
