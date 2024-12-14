import javax.swing.*;
import java.awt.*;

public class HorseRacingGame extends JFrame {
    public HorseRacingGame() {
        setTitle("Inside Track");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        Player player = new Player("Player 1", 1000); // Membuat objek Player

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
        new HorseRacingGame();
    }
}
