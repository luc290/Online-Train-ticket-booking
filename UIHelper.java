
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UIHelper {

    public static final Color NAVY = new Color(15, 39, 71);
    public static final Color BLUE = new Color(31, 105, 190);
    public static final Color LIGHT = new Color(242, 246, 251);
    public static final Color TEXT = new Color(45, 55, 72);
    public static final Color GREEN = new Color(28, 135, 94);
    public static final Color BORDER = new Color(218, 226, 237);

    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(BLUE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new CompoundBorder(
            new LineBorder(BLUE, 1, true),
            new EmptyBorder(9, 16, 9, 16)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 25));
        l.setForeground(NAVY);
        return l;
    }

    public static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(TEXT);
        f.setBackground(Color.WHITE);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return f;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(18, 18, 18, 18)
        ));
        return p;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(222, 237, 255));
        table.setSelectionForeground(NAVY);
        table.setGridColor(new Color(232, 237, 244));
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(
            new Dimension(0, 38)
        );
    }

    public static JPanel page(String title) {
        JPanel p = new JPanel(new BorderLayout(18, 18));
        p.setBackground(LIGHT);
        p.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(heading(title), BorderLayout.WEST);

        p.add(titlePanel, BorderLayout.NORTH);
        return p;
    }

    public static void message(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent, message, "RailEase",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
