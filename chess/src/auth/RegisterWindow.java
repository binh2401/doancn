package auth;

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

    public RegisterWindow() {
        setTitle("Đăng ký");
        setSize(300, 250);
        setLayout(new GridLayout(4, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Giao diện người dùng
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordField = new JPasswordField();
        registerButton = new JButton("Đăng ký");
        cancelButton = new JButton("Hủy");

        // Thêm các thành phần vào cửa sổ
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(registerButton);
        add(cancelButton);

        // Hành động khi bấm vào nút đăng ký
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Lưu thông tin người dùng vào hệ thống (có thể ghi vào database)
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Đăng ký thành công!");
                    dispose(); // Đóng cửa sổ sau khi đăng ký thành công
                }
            }
        });

        // Hành động khi bấm vào nút hủy
        cancelButton.addActionListener(e -> dispose());
        setLocationRelativeTo(null);
    }
}