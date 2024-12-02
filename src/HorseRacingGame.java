import javax.swing.*;
import java.awt.*;

public class HorseRacingGame extends JFrame {
    public HorseRacingGame() {
        setTitle("Pacuan Kuda");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Membuat objek Race
        Race race = new Race();  

        // Main panel menggunakan CardLayout untuk mengganti tampilan
        JPanel mainPanel = new JPanel(new CardLayout());
        add(mainPanel);

        // Membuat panel untuk LandingPage dan Gameplay
        GameplayPanel gameplayPanel = new GameplayPanel(race);  
        LandingPage landingPage = new LandingPage(mainPanel, gameplayPanel);

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
