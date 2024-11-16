package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    private void createTableIfNotExists() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Users (
                    id VARCHAR(255) PRIMARY KEY,
                    username VARCHAR(255) NOT NULL,
                    gamesPlayed INT DEFAULT 0,
                    gamesWon INT DEFAULT 0
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Checked and ensured table Users exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveUser(User user) {
        String sql = "INSERT INTO Users (id, username, gamesPlayed, gamesWon) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setInt(3, user.getGamesPlayed());
            statement.setInt(4, user.getGamesWon());
            statement.executeUpdate();
            System.out.println("User saved to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
