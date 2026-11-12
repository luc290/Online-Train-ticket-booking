
import javax.swing.*;
import java.awt.*;

public class DashboardPage extends JFrame {

    private String user;

    public DashboardPage(String user) {

        this.user = user;

        setTitle("RailEase - Dashboard");
        setSize(450, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(5, 1, 12, 12));

        p.setBorder(
            BorderFactory.createEmptyBorder(25, 50, 25, 50)
        );

        JLabel title = new JLabel(
            "Welcome to RailEase",
            SwingConstants.CENTER
        );

        title.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        JButton book = new JButton("BOOK TICKET");
        JButton my = new JButton("MY BOOKINGS");
        JButton about = new JButton("ABOUT");
        JButton logout = new JButton("LOGOUT");

        p.add(title);
        p.add(book);
        p.add(my);
        p.add(about);
        p.add(logout);

        add(p);

        book.addActionListener(e -> {
            new SearchTrain(user);
        });

        my.addActionListener(e -> {
            new MyBookingPage(user);
        });

        about.addActionListener(e -> {
            new AboutPage();
        });

        logout.addActionListener(e -> {

            dispose();
            new LoginPage();
        });

        setVisible(true);
    }
}
