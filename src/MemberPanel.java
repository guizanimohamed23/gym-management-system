import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class MemberPanel extends JPanel {
    public MemberPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 5));
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Monthly", "Yearly"});
        JTextField startField = new JTextField("YYYY-MM-DD");

        JButton addBtn = new JButton("Add member");
        JButton viewBtn = new JButton("View members");
        JButton deleteBtn = new JButton("Delete membre");

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Age:")); formPanel.add(ageField);
        formPanel.add(new JLabel("Gender:")); formPanel.add(genderField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);
        formPanel.add(new JLabel("Membership Type:")); formPanel.add(typeBox);
        formPanel.add(new JLabel("Start Date:")); formPanel.add(startField);
        formPanel.add(new JLabel("")); formPanel.add(addBtn);
        formPanel.add(viewBtn); formPanel.add(deleteBtn);

        add(formPanel, BorderLayout.NORTH);

        JTable table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO members(name, age, gender, phone, membership_type, start_date, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                LocalDate start = LocalDate.parse(startField.getText());
                LocalDate expiry = typeBox.getSelectedItem().toString().equalsIgnoreCase("Monthly") ? start.plusMonths(1) : start.plusYears(1);

                stmt.setString(1, nameField.getText());
                stmt.setInt(2, Integer.parseInt(ageField.getText()));
                stmt.setString(3, genderField.getText());
                stmt.setString(4, phoneField.getText());
                stmt.setString(5, typeBox.getSelectedItem().toString());
                stmt.setDate(6, Date.valueOf(start));
                stmt.setDate(7, Date.valueOf(expiry));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Member added");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed ");
            }
        });

        viewBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM members");
                 ResultSet rs = stmt.executeQuery()) {

                table.setModel(buildTableModel(rs));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {
            String nameToDelete = JOptionPane.showInputDialog(this, " name to delete:");
            if (nameToDelete != null && !nameToDelete.isBlank()) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM members WHERE name = ?")) {
                    stmt.setString(1, nameToDelete.trim());
                    int rows = stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, rows > 0 ? "Deleted" : " not found.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) columnNames.add(meta.getColumnName(i));

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) row.add(rs.getObject(i));
            data.add(row);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}
