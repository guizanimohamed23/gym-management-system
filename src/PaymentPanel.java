import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;


public class PaymentPanel extends JPanel {
    public PaymentPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        JTextField memberNameField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField("YYYY-MM-DD");
        JButton addBtn = new JButton("Add Payment");
        JButton viewBtn = new JButton("View Payments");

        formPanel.add(new JLabel("Member Name:")); formPanel.add(memberNameField);
        formPanel.add(new JLabel("Amount:")); formPanel.add(amountField);
        formPanel.add(new JLabel("Payment Date:")); formPanel.add(dateField);
        formPanel.add(addBtn); formPanel.add(viewBtn);

        add(formPanel, BorderLayout.NORTH);

        JTable table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String memberName = memberNameField.getText().trim();

                int memberId = -1;
                String findMemberSql = "SELECT id FROM members WHERE name = ?";
                try (PreparedStatement stmt = conn.prepareStatement(findMemberSql)) {
                    stmt.setString(1, memberName);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        memberId = rs.getInt("id");
                    } else {
                        JOptionPane.showMessageDialog(this, "not found");
                        return;
                    }
                }

                String insertSql = "INSERT INTO payments (member_id, amount, payment_date) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setInt(1, memberId);
                    stmt.setBigDecimal(2, new java.math.BigDecimal(amountField.getText()));
                    stmt.setDate(3, Date.valueOf(LocalDate.parse(dateField.getText())));
                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Payment added ");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        viewBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM payments");
                 ResultSet rs = stmt.executeQuery()) {
                table.setModel(MemberPanel.buildTableModel(rs));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
