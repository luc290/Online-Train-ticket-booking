
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PassengerPage extends JFrame {

    private String user;
    private Train train;
    private String date;

    private JComboBox<String> quota =
        new JComboBox<>(DataManager.QUOTAS);

    private JComboBox<String> travelClass =
        new JComboBox<>(DataManager.CLASSES);

    private JTextField passenger = new JTextField(18);
    private JTextField age = new JTextField(5);

    private JLabel fareLabel = new JLabel();

    private DefaultTableModel availabilityModel;

    public PassengerPage(String user, Train train, String date) {

        this.user = user;
        this.train = train;
        this.date = date;

        setTitle("RailEase - Passenger and Booking");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel details = new JPanel(new GridLayout(0, 2, 8, 8));

        details.setBorder(
            BorderFactory.createTitledBorder("Journey Details")
        );

        details.add(new JLabel("Train:"));
        details.add(new JLabel(train.number + " - " + train.name));

        details.add(new JLabel("Route:"));
        details.add(new JLabel(train.source + " to " + train.destination));

        details.add(new JLabel("Journey date:"));
        details.add(new JLabel(date));

        details.add(new JLabel("Booking quota:"));
        details.add(quota);

        details.add(new JLabel("Travel class:"));
        details.add(travelClass);

        details.add(new JLabel("Passenger name:"));
        details.add(passenger);

        details.add(new JLabel("Passenger age:"));
        details.add(age);

        details.add(new JLabel("Estimated fare:"));
        details.add(fareLabel);

        String[] cols = {
            "Class",
            "Confirmed Available",
            "RAC Available",
            "Waiting List Count",
            "General Fare",
            "Tatkal Fare"
        };

        availabilityModel = new DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable availabilityTable =
            new JTable(availabilityModel);

        JButton refresh = new JButton("Refresh Availability");
        JButton book = new JButton("Confirm Booking");

        JPanel bottom = new JPanel();

        bottom.add(refresh);
        bottom.add(book);

        add(details, BorderLayout.NORTH);
        add(new JScrollPane(availabilityTable), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        quota.addActionListener(e -> updateScreen());

        travelClass.addActionListener(e -> updateFare());

        refresh.addActionListener(e -> updateScreen());

        book.addActionListener(e -> bookTicket());

        updateScreen();

        setVisible(true);
    }

    private void updateFare() {

        String cls = (String) travelClass.getSelectedItem();

        fareLabel.setText(
            "₹" + DataManager.fare(
                train,
                cls,
                (String) quota.getSelectedItem()
            )
        );
    }

    private void updateScreen() {

        String q = (String) quota.getSelectedItem();

        if ("Tatkal".equals(q) &&
            travelClass.getSelectedIndex() == 3) {

            travelClass.setSelectedIndex(0);
        }

        updateFare();

        availabilityModel.setRowCount(0);

        for (int i = 0; i < DataManager.CLASSES.length; i++) {

            String cls = DataManager.CLASSES[i];

            boolean tatkalDisabled =
                "Tatkal".equals(q) && i == 3;

            int available = tatkalDisabled ? 0 :
                DataManager.confirmedAvailable(
                    train, date, cls, q
                );

            int rac = tatkalDisabled ? 0 :
                DataManager.racAvailable(
                    train, date, cls, q
                );

            int waiting = 0;

            for (Booking b : DataManager.userBookings(user)) {

                if (b.trainNumber.equals(train.number) &&
                    b.date.equals(date) &&
                    b.travelClass.equals(cls) &&
                    b.quota.equals(q) &&
                    b.status.startsWith("WL")) {

                    waiting++;
                }
            }

            availabilityModel.addRow(new Object[] {

                cls,

                tatkalDisabled ? "Not available" : available,

                tatkalDisabled ? "-" : rac,

                tatkalDisabled ? "-" : waiting,

                "₹" + DataManager.fare(
                    train, cls, "General"
                ),

                i == 3 ? "Not available" :
                    "₹" + DataManager.fare(
                        train, cls, "Tatkal"
                    )
            });
        }
    }

    private void bookTicket() {

        try {

            LocalDate.parse(date);

            String name = passenger.getText().trim();

            int a = Integer.parseInt(age.getText().trim());

            if (name.isEmpty() || a < 1 || a > 120) {

                throw new IllegalArgumentException(
                    "Enter a valid passenger name and age."
                );
            }

            String q = (String) quota.getSelectedItem();

            String cls = (String) travelClass.getSelectedItem();

            if ("Tatkal".equals(q) &&
                "AC First Class (1A)".equals(cls)) {

                throw new IllegalArgumentException(
                    "Tatkal is not available in First AC (1A)."
                );
            }

            if ("Tatkal".equals(q) &&
                LocalDate.parse(date).isBefore(
                    LocalDate.now().plusDays(1)
                )) {

                throw new IllegalArgumentException(
                    "Tatkal demo booking is allowed only at least one day before journey date."
                );
            }

            Booking b = DataManager.book(
                user,
                train,
                name,
                a,
                date,
                cls,
                q
            );

            updateScreen();

            JOptionPane.showMessageDialog(
                this,

                "Booking saved!\n" +
                "Booking ID: " + b.id + "\n" +
                "Status: " + b.status + "\n" +
                "Coach: " + b.coach + "\n" +
                "Seat/Berth: " + b.seat + "\n" +
                "Fare: ₹" + b.fare + "\n\n" +
                "This is a simulated college project booking."
            );

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Enter date in YYYY-MM-DD format."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage()
            );
        }
    }
}
