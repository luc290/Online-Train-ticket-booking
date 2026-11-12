
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SearchTrain extends JFrame {

    private String user;

    private JTextField from = new JTextField(10);
    private JTextField to = new JTextField(10);

    private JTextField date =
        new JTextField("2026-12-01", 10);

    private DefaultTableModel model;
    private JTable table;

    private ArrayList<Train> shown = new ArrayList<>();

    public SearchTrain(String user) {

        this.user = user;

        setTitle("Search Trains and Fares");
        setSize(1100, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel top = new JPanel();

        top.add(new JLabel("From:"));
        top.add(from);

        top.add(new JLabel("To:"));
        top.add(to);

        top.add(new JLabel("Journey date (YYYY-MM-DD):"));
        top.add(date);

        JButton search = new JButton("Search");

        top.add(search);

        String[] cols = {
            "Train No.",
            "Train Name",
            "From",
            "To",
            "Distance",
            "SL Fare",
            "3A Fare",
            "2A Fare",
            "1A Fare"
        };

        model = new DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton select = new JButton(
            "Select Train and Continue"
        );

        add(select, BorderLayout.SOUTH);

        search.addActionListener(e -> {
            searchTrains();
        });

        select.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please select a train."
                );

                return;
            }

            new PassengerPage(
                user,
                shown.get(row),
                date.getText().trim()
            );
        });

        searchTrains();

        setVisible(true);
    }

    private void searchTrains() {

        model.setRowCount(0);
        shown.clear();

        String f = from.getText().trim().toLowerCase();
        String t = to.getText().trim().toLowerCase();

        for (Train tr : TrainCatalog.allTrains()) {

            if ((f.isEmpty() ||
                tr.source.toLowerCase().contains(f)) &&

                (t.isEmpty() ||
                tr.destination.toLowerCase().contains(t))) {

                shown.add(tr);

                model.addRow(new Object[] {

                    tr.number,
                    tr.name,
                    tr.source,
                    tr.destination,
                    tr.distanceKm + " km",

                    "₹" + DataManager.fare(
                        tr,
                        DataManager.CLASSES[0],
                        "General"
                    ),

                    "₹" + DataManager.fare(
                        tr,
                        DataManager.CLASSES[1],
                        "General"
                    ),

                    "₹" + DataManager.fare(
                        tr,
                        DataManager.CLASSES[2],
                        "General"
                    ),

                    "₹" + DataManager.fare(
                        tr,
                        DataManager.CLASSES[3],
                        "General"
                    )
                });
            }
        }

        if (shown.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No sample route found. Try another source or destination."
            );
        }
    }
}
