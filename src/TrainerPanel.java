import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class TrainerPanel extends JPanel {
    public TrainerPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        JTextField nameField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton addBtn = new JButton("Add Trainer");
        JButton viewBtn = new JButton("View Trainers");
        JButton deleteBtn = new JButton("Delete Trainer by Name");

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Specialization:")); formPanel.add(specializationField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);
        formPanel.add(addBtn); formPanel.add(viewBtn);
        formPanel.add(deleteBtn); formPanel.add(new JLabel());

        add(formPanel, BorderLayout.NORTH);

        JTable table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO trainers(name, specialization, phone) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, specializationField.getText());
                stmt.setString(3, phoneField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Trainer added");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed ");
            }
        });

        viewBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM trainers");
                 ResultSet rs = stmt.executeQuery()) {
                table.setModel(MemberPanel.buildTableModel(rs));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {
            String nameToDelete = JOptionPane.showInputDialog(this, "Enter  name to delete:");
            if (nameToDelete != null && !nameToDelete.isBlank()) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM trainers WHERE name = ?")) {
                    stmt.setString(1, nameToDelete.trim());
                    int rows = stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, rows > 0 ? "Deleted" : " not found");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
