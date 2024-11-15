package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class UserDAO {
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
