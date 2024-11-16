package model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class User {
    private static final Set<String> usedIds = new HashSet<>(); // Lưu các ID đã sử dụng
    private static final Random random = new Random();

    private String id;
    private String username;
    private String password;
    private int gamesPlayed;
    private int gamesWon;

    public User(String username, String password) {
        this.id = generateUniqueId(); // Tạo ID không trùng lặp
        this.username = username;    // Lấy username từ tham số
        this.password = password;    // Lấy password từ tham số
        this.gamesPlayed = 0;
        this.gamesWon = 0;
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
