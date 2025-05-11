import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class login extends JFrame {
    public login() {
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);



        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");




        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);



        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, userField.getText());
                stmt.setString(2, new String(passField.getPassword()));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    dispose();
                    new maindashb();
                } else {
                    JOptionPane.showMessageDialog(this, "wrong input");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        add(panel);
        setVisible(true);
    }
}
