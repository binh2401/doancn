package auth;

import util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    public LoginWindow() {
        setTitle("Đăng nhập");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Giao diện người dùng
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Đăng nhập");
        cancelButton = new JButton("Hủy");

        // Thêm các thành phần vào cửa sổ
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(cancelButton);

        // Hành động khi bấm vào nút đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Kiểm tra logic đăng nhập
                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Đăng nhập thành công!");
                    dispose(); // Đóng cửa sổ sau khi đăng nhập thành công
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Sai thông tin đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Hành động khi bấm vào nút hủy
        cancelButton.addActionListener(e -> dispose());
        setLocationRelativeTo(null);
    }

    // Phương thức kiểm tra thông tin đăng nhập từ cơ sở dữ liệu
    private boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra nếu có dữ liệu trả về, tức là tài khoản hợp lệ
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}