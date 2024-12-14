import javax.swing.*;
import java.awt.*;

public class InsideTrack extends JFrame {
    public InsideTrack() {
        setTitle("Inside Track");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Masukkan nama pemain dan poin awal
        String playerName = JOptionPane.showInputDialog("Enter your name:");
        int initialPoints = 1000; // Default poin awal

        Player player = new Player(playerName, initialPoints); // Buat atau ambil pemain dari database

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
}
