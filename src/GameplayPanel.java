import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.List;
import java.awt.image.BufferedImage; // Add this import


public class GameplayPanel extends JPanel implements Runnable {
    private Image backgroundImage;
    private int xPosition; // Posisi X gambar
    private int moveSpeed = 3; 
    private boolean startAnimation = false; 
    private boolean raceFinished = false; 
    private Race race; 
    private List<Horse> horses; 
    private RacePanel racePanel; 

    public GameplayPanel(Race race, RacePanel racePanel) {
        this.race = race;
        this.horses = race.getRunners(); 
        this.racePanel = racePanel; 

        // Load gambar background
        ImageIcon icon = new ImageIcon("background.png");
        backgroundImage = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            g.drawImage(backgroundImage, xPosition, 0, panelWidth, panelHeight, null);
            g.drawImage(backgroundImage, xPosition + panelWidth, 0, panelWidth, panelHeight, null);
        }

        // Gambar kuda-kuda dan jaraknya
        for (Horse horse : horses) {
            int horseX = (int) (horse.getDistance() * getWidth() / Race.RACE_LENGTH);
            int horseY = 50 + horses.indexOf(horse) * 70; 

            int animStep = (int) (horse.getDistance() / Race.RACE_LENGTH * 12); 
            BufferedImage horseImage = racePanel.getImage(animStep);

            // Gambar kuda dengan gambar dari RacePanel
            if (horseImage != null) {
                g.drawImage(horseImage, horseX, horseY, horseImage.getWidth(), horseImage.getHeight(), null);
            }
        }
    }


    // Method untuk memulai animasi dan balapan
    public void startAnimation() {
        if (!startAnimation) {
            startAnimation = true;
            Thread animationThread = new Thread(this);
            animationThread.start(); // Mulai animasi latar belakang
            Thread raceThread = new Thread(() -> {
                Horse winner = race.race(); // Mulai balapan dan cari pemenangnya
                raceFinished = true; // Tandai balapan selesai
                repaint(); // Repaint panel setelah balapan selesai
            });
            raceThread.start();
        }
    }

    @Override
    public void run() {
        // Animasi background bergerak, tapi berhenti jika balapan selesai
        while (startAnimation) {
            if (!raceFinished) {
                // Update posisi X untuk gerakan background hanya jika balapan belum selesai
                xPosition -= moveSpeed;

                // Reset posisi jika gambar sepenuhnya keluar dari layar
                if (xPosition <= -getWidth()) {
                    xPosition = 0;
                }

                // Memanggil stepAnimation untuk memperbarui langkah animasi setiap frame
                racePanel.stepAnimation(); // Memperbarui langkah animasi setiap frame
            }

            // Repaint panel untuk memperbarui tampilan
            repaint();

            // Delay untuk mengontrol kecepatan animasi
            try {
                Thread.sleep(16); // Sekitar 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
