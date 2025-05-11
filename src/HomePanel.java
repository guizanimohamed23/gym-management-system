import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private Image backgroundImage;

    public HomePanel() {

        backgroundImage = new ImageIcon(getClass().getResource("/gym imag.jpg")).getImage();


        setLayout(new BorderLayout());

        JLabel welcome = new JLabel("Welcome to Gym Management System", JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setForeground(Color.WHITE);
        welcome.setOpaque(false);

        add(welcome, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
