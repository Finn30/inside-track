import javax.swing.*;
import java.awt.*;

public class InsideTrack extends JFrame {
    public InsideTrack() {
        setTitle("Inside Track");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        String username = null;

        while (username == null) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Welcome to Inside Track! Choose an option:",
                    "Login/Register",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] { "Login", "Register" },
                    "Login");

            if (choice == JOptionPane.YES_OPTION) { // Login
                username = login();
            } else if (choice == JOptionPane.NO_OPTION) { // Register
                register();
            } else {
                System.exit(0); // Exit if the user closes the dialog
            }
        }

        // Masukkan nama pemain dan poin awal
        // String playerName = JOptionPane.showInputDialog("Enter your name:");

        int initialPoints = PlayerDB.getPlayerPoints(username);
        Player player = new Player(username, initialPoints);

        // Membuat objek Race
        Race race = new Race();

        // Main panel menggunakan CardLayout untuk mengganti tampilan
        JPanel mainPanel = new JPanel(new CardLayout());
        add(mainPanel);

        // Membuat panel untuk LandingPage dan Gameplay
        GameplayPanel gameplayPanel = new GameplayPanel(mainPanel, race, player);
        LandingPage landingPage = new LandingPage(mainPanel, gameplayPanel, player);

        // Menambahkan panel-panel ke dalam mainPanel
        mainPanel.add(landingPage, "Landing");
        mainPanel.add(gameplayPanel, "Gameplay");

        // Menampilkan jendela
        setVisible(true);
    }

    public static void main(String[] args) {
        // Memulai aplikasi
        new InsideTrack();
    }

    private String login() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (PlayerDB.login(username, password)) {
            JOptionPane.showMessageDialog(null, "Login successful!");

            // Ambil poin pemain dari database
            int currentPoints = PlayerDB.getPlayerPoints(username);

            // Tampilkan dialog untuk menambah poin
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Welcome back, " + username + "!\nYou have " + currentPoints
                            + " points.\nWould you like to add more points?",
                    "Add Points",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] { "Yes", "No" },
                    "No");

            if (choice == JOptionPane.YES_OPTION) {
                String pointsInput = JOptionPane.showInputDialog("Enter points to add:");
                try {
                    int pointsToAdd = Integer.parseInt(pointsInput);
                    if (pointsToAdd > 0) {
                        PlayerDB.updatePlayerPoints(username, currentPoints + pointsToAdd);
                        JOptionPane.showMessageDialog(null, pointsToAdd + " points added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Points must be greater than 0!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Points not added.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            return username;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void register() {
        String username = JOptionPane.showInputDialog("Choose a username:");
        String password = JOptionPane.showInputDialog("Choose a password:");

        if (PlayerDB.register(username, password)) {
            JOptionPane.showMessageDialog(null, "Registration successful! You can now log in.");
        } else {
            JOptionPane.showMessageDialog(null, "Registration failed! Username might already exist.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
