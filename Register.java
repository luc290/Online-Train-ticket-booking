
import javax.swing.*;
import java.awt.*;

public class Register extends JDialog {

    public Register(JFrame parent) {

        super(parent, "Create Account", true);

        JTextField user = new JTextField(15);
        JPasswordField pass = new JPasswordField(15);

        JPanel p = new JPanel(new GridLayout(3, 2, 8, 8));

        p.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        p.add(new JLabel("Username:"));
        p.add(user);

        p.add(new JLabel("Password:"));
        p.add(pass);

        JButton save = new JButton("Create Account");

        p.add(new JLabel());
        p.add(save);

        add(p);

        save.addActionListener(e -> {

            if (DataManager.register(
                    user.getText(),
                    new String(pass.getPassword()))) {

                JOptionPane.showMessageDialog(
                    this,
                    "Registration successful. Please login."
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Username already exists or fields are empty."
                );
            }
        });

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
