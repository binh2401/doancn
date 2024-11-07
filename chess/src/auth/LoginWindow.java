package auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    // Giả sử phương thức này kiểm tra thông tin đăng nhập (có thể thay thế bằng cách kết nối database)
    private boolean authenticate(String username, String password) {
        // Kiểm tra thông tin tài khoản mẫu (thay thế bằng kiểm tra database thực tế)
        return "user".equals(username) && "password".equals(password);
    }
}
