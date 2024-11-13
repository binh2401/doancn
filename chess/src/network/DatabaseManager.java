package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
public class DatabaseManager {
    private static final String URL = "jdbc:sqlserver://DESKTOP-KCNRSL8\\MSSQLSERVER01:1433;databaseName=chess;integratedSecurity=true;trustServerCertificate=true";  // Kết nối tới SQL Server
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static void main(String[] args) {
        try {
            // Tải JDBC driver
            Class.forName(DRIVER);

            // Kết nối tới SQL Server
            Connection connection = DriverManager.getConnection(URL);

            // Tạo đối tượng Statement để thực thi các câu lệnh SQL
            Statement stmt = connection.createStatement();

            // Tạo user mới
            String username = "newUser";
            String password = "newPassword123";
            String createUserQuery = "CREATE LOGIN " + username + " WITH PASSWORD = '" + password + "';";

            // Tạo database mới cho user
            String databaseName = username + "DB";  // Đặt tên database theo tên user
            String createDatabaseQuery = "CREATE DATABASE " + databaseName + ";";

            // Cấp quyền cho user truy cập vào database
            String grantPermissionsQuery = "USE " + databaseName + "; GRANT ALL PRIVILEGES TO " + username + ";";

            // Thực thi các câu lệnh SQL
            stmt.executeUpdate(createUserQuery);
            stmt.executeUpdate(createDatabaseQuery);
            stmt.executeUpdate(grantPermissionsQuery);

            System.out.println("User và Database đã được tạo thành công!");

            // Đóng kết nối
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
