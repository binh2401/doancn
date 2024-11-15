package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseManager {
    public static void main(String[] args) {
        // Kết nối tới MySQL trên Laragon với tài khoản root
        String rootUrl = "jdbc:mysql://localhost:3306/"; // Nếu Laragon dùng cổng khác, thêm ?serverTimezone=UTC vào cuối URL
        String rootUser = "root"; // Tài khoản mặc định của Laragon
        String rootPassword = ""; // Mật khẩu mặc định của Laragon (trống)

        // Tên user và database sẽ được tạo
        String newUsername = "new_user";
        String newPassword = "password123";
        String newDatabase = "cheessss";

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
