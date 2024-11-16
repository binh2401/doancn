package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {
    private List<User> users = new ArrayList<>();

    // Thêm người dùng với id ngẫu nhiên
    public void addUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Tạo ID ngẫu nhiên
                String randomId = UUID.randomUUID().toString();  // Sử dụng UUID để tạo ID ngẫu nhiên
                statement.setString(1, randomId);  // Chèn ID ngẫu nhiên
                statement.setString(2, user.getUsername());
                statement.setString(3, user.getPassword());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding user to the database.");
        }
    }

    // Kiểm tra xem username có tồn tại trong cơ sở dữ liệu không
    public boolean isUsernameTaken(String username) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                return rs.next();  // Nếu có dữ liệu, tức là username đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking if username exists.");
        }
        return false;
    }

    // Lấy tất cả người dùng từ cơ sở dữ liệu
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    users.add(new User(username, password));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving users from database.");
        }
        return users;
    }
}
