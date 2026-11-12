
import javax.swing.*;

public class AboutPage extends JFrame {

    public AboutPage() {

        setTitle("About RailEase");
        setSize(520, 300);
        setLocationRelativeTo(null);

        JTextArea text = new JTextArea(

            "RailEase - Online Train Ticket Booking System\n\n" +

            "Features:\n" +

            "- User registration and login\n" +
            "- Search sample routes across India\n" +
            "- Class-wise estimated fares: SL, 3A, 2A and 1A\n" +
            "- General and Tatkal demo quota\n" +
            "- Class-wise seat availability\n" +
            "- Coach and seat allocation\n" +
            "- RAC and waiting-list simulation\n" +
            "- Booking history and cancellation\n\n" +

            "Important: This is a college mini-project. " +
            "Train routes, fares, seats, Tatkal quota and " +
            "bookings are simulated. It is not connected " +
            "to IRCTC and cannot make real railway reservations."
        );

        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        add(new JScrollPane(text));

        setVisible(true);
    }
}
