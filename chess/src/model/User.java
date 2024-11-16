package model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
public class User {
    private static final Set<String> usedIds = new HashSet<>(); // Lưu các ID đã sử dụng
    private static final Random random = new Random();

    private String id;
    private String username;
    private String password;
    private int gamesPlayed;
    private int gamesWon;
    private static final String URL = "jdbc:mysql://localhost:3306/chess?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public User(String username, String password) {
        this.id = generateUniqueId(); // Tạo ID không trùng lặp
        this.username = username;    // Lấy username từ tham số
        this.password = password;    // Lấy password từ tham số
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        createTableIfNotExists();
    }

    // Tạo ID 5 số ngẫu nhiên không trùng lặp
    private String generateUniqueId() {
        String newId;
        do {
            newId = String.format("%05d", random.nextInt(100000)); // Tạo ID 5 chữ số
        } while (usedIds.contains(newId));
        usedIds.add(newId); // Thêm ID vào danh sách đã sử dụng
        return newId;
    }
    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id VARCHAR(36) PRIMARY KEY, "   // ID kiểu chuỗi (có thể là UUID, chiều dài 36 ký tự)
                + "username VARCHAR(255) NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "gamesPlayed INT DEFAULT 0, "
                + "gamesWon INT DEFAULT 0"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'users' is ready.");
        } catch (SQLException e) {
            System.out.println("Error while creating the table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Getters và Setters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
