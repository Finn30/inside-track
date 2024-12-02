import javax.swing.*;
import java.awt.*;

public class LandingPage extends JPanel {

    public LandingPage(JPanel mainPanel, GameplayPanel gameplayPanel) {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Selamat Datang di Game Pacuan Kuda!", JLabel.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Mulai");
        add(startButton, BorderLayout.SOUTH);
        
        startButton.addActionListener(e -> {
            // Ganti panel ke Gameplay
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Gameplay");
            
            // Mulai animasi background setelah GameplayPanel ditampilkan
            gameplayPanel.startAnimation();
        });
    }
}
