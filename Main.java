
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        DataManager.initialize();

        SwingUtilities.invokeLater(() -> {
            new LoginPage();
        });
    }
}
