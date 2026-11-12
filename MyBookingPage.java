
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyBookingPage extends JFrame {

    private String user;

    private DefaultTableModel model;
    private JTable table;

    private List<Booking> bookings;

    public MyBookingPage(String user) {

        this.user = user;

        setTitle("My Bookings");
        setSize(1100, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {
            "Booking ID",
            "Train",
            "Route",
            "Passenger",
            "Age",
            "Date",
            "Class",
            "Quota",
            "Coach",
            "Seat",
            "Status",
            "Fare"
        };

        model = new DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        JButton cancel =
            new JButton("Cancel Selected Booking");

        JButton refresh =
            new JButton("Refresh");

        JPanel p = new JPanel();

        p.add(refresh);
        p.add(cancel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(p, BorderLayout.SOUTH);

        refresh.addActionListener(e -> load());

        cancel.addActionListener(e -> cancelSelected());

        load();

        setVisible(true);
    }

    private void load() {

        model.setRowCount(0);

        bookings = DataManager.userBookings(user);

        for (Booking b : bookings) {

            model.addRow(new Object[] {

                b.id,
                b.trainName,
                b.source + " to " + b.destination,
                b.passenger,
                b.age,
                b.date,
                b.travelClass,
                b.quota,
                b.coach,
                b.seat,
                b.status,
                "₹" + b.fare
            });
        }
    }

    private void cancelSelected() {

        int row = table.getSelectedRow();

        if (row < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Select a booking first."
            );

            return;
        }

        Booking b = bookings.get(row);

        if (DataManager.cancel(b.id, user)) {

            JOptionPane.showMessageDialog(
                this,
                "Booking cancelled."
            );

            load();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Unable to cancel booking."
            );
        }
    }
}
