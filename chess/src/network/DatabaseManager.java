package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseManager {
    public static void main(String[] args) {
        // Kết nối tới MySQL với tài khoản root (hoặc tài khoản có quyền admin)
        String rootUrl = "jdbc:mysql://localhost:3306/"; // Không cần chỉ định database
        String rootUser = "root"; // Tài khoản admin mặc định
        String rootPassword = ""; // Mật khẩu mặc định (trống với XAMPP)

        // Tên user và database sẽ được tạo
        String newUsername = "new_user";
        String newPassword = "password123";
        String newDatabase = "new_database";

        try (Connection connection = DriverManager.getConnection(rootUrl, rootUser, rootPassword);
             Statement statement = connection.createStatement()) {

            // Tạo user mới
            String createUserQuery = "CREATE USER '" + newUsername + "'@'localhost' IDENTIFIED BY '" + newPassword + "';";
            statement.executeUpdate(createUserQuery);
            System.out.println("Tạo user '" + newUsername + "' thành công!");

            // Tạo database mới
            String createDatabaseQuery = "CREATE DATABASE " + newDatabase + ";";
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Tạo database '" + newDatabase + "' thành công!");

            // Gán quyền cho user trên database
            String grantPrivilegesQuery = "GRANT ALL PRIVILEGES ON " + newDatabase + ".* TO '" + newUsername + "'@'localhost';";
            statement.executeUpdate(grantPrivilegesQuery);
            System.out.println("Gán quyền cho user '" + newUsername + "' trên database '" + newDatabase + "' thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }
}
