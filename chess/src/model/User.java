package model;

import java.util.UUID;

public class User {
    private String id;
    private String username;
    private int gamesPlayed;
    private int gamesWon;

    public User() {
        this.id = UUID.randomUUID().toString(); // Tạo ID ngẫu nhiên
        this.username = generateRandomUsername();
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }

    private String generateRandomUsername() {
        return "User" + System.currentTimeMillis(); // Tạo username ngẫu nhiên
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
