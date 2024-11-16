package auth;

import model.User;
import model.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton cancelButton;
    private UserManager userManager;

    public RegisterWindow(UserManager userManager) {
        if (userManager == null) {
            throw new IllegalArgumentException("UserManager không được để null.");
        }

        this.userManager = userManager;

        setTitle("Đăng ký");
        setSize(300, 250);
        setLayout(new GridLayout(4, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Cài đặt giao diện
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordField = new JPasswordField();
        registerButton = new JButton("Đăng ký");
        cancelButton = new JButton("Hủy");

        // Thêm các thành phần vào giao diện
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(registerButton);
        add(cancelButton);

        // Xử lý nút Đăng ký
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (userManager.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                userManager.addUser(new User(username, password));
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                dispose();
            }
        });

        // Xử lý nút Hủy
        cancelButton.addActionListener(e -> dispose());
        setLocationRelativeTo(null);
    }
}