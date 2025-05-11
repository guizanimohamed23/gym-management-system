import javax.swing.*;
import java.awt.*;

public class maindashb extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    public maindashb() {
        setTitle("Gym Management Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        JButton homeBtn = new JButton("Home");
        JButton memberBtn = new JButton("Members");
        JButton trainerBtn = new JButton("Trainers");
        JButton paymentBtn = new JButton("Payments");


        JPanel navPanel = new JPanel(new GridLayout(1, 5));
        navPanel.add(homeBtn);
        navPanel.add(memberBtn);
        navPanel.add(trainerBtn);

        navPanel.add(paymentBtn);

        cardPanel.add(new HomePanel(), "Home");
        cardPanel.add(new MemberPanel(), "Members");
        cardPanel.add(new TrainerPanel(), "Trainers");

        cardPanel.add(new PaymentPanel(), "Payments");

        homeBtn.addActionListener(e -> cardLayout.show(cardPanel, "Home"));
        memberBtn.addActionListener(e -> cardLayout.show(cardPanel, "Members"));
        trainerBtn.addActionListener(e -> cardLayout.show(cardPanel, "Trainers"));
        paymentBtn.addActionListener(e -> cardLayout.show(cardPanel, "Payments"));

        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
