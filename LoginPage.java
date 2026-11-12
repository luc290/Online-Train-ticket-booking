
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    private JTextField username = new JTextField(18);
    private JPasswordField password = new JPasswordField(18);

    public LoginPage() {

        setTitle("RailEase - Login");
        setSize(400, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));

        p.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        p.add(new JLabel("Username:"));
        p.add(username);

        p.add(new JLabel("Password:"));
        p.add(password);

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        p.add(login);
        p.add(register);

        add(p);

        login.addActionListener(e -> {

            if (DataManager.login(
                    username.getText(),
                    new String(password.getPassword()))) {

                dispose();

                new DashboardPage(username.getText().trim());

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password."
                );
            }
        });

        register.addActionListener(e -> {
            new Register(this);
        });

        setVisible(true);
    }
}
