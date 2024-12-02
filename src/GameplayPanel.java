import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.List;

public class GameplayPanel extends JPanel implements Runnable {
    private Image backgroundImage;
    private int xPosition; // Posisi X gambar
    private int moveSpeed = 3; // Kecepatan pergerakan gambar
    private boolean startAnimation = false; // Flag untuk memulai animasi
    private boolean raceFinished = false; // Flag untuk mengetahui apakah balapan selesai
    private Race race; // Referensi ke objek Race
    private List<Horse> horses; // List kuda

    public GameplayPanel(Race race) {
        this.race = race;
        this.horses = race.getRunners(); // Ambil daftar kuda

        // Load gambar background
        ImageIcon icon = new ImageIcon("C:/Users/gusbr/OneDrive/Gambar/Cool Yeah/Semester 5/PBO/Praktikum/Project/inside-track/assets/img/background.png");
        backgroundImage = icon.getImage();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            // Gambar background hanya jika balapan belum selesai
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Gambar dua kali untuk looping (efek infinite scroll)
            g.drawImage(backgroundImage, xPosition, 0, panelWidth, panelHeight, null);
            g.drawImage(backgroundImage, xPosition + panelWidth, 0, panelWidth, panelHeight, null);
        }

        // Gambar kuda-kuda dan jaraknya
        for (Horse horse : horses) {
            int horseX = (int) (horse.getDistance() * getWidth() / Race.RACE_LENGTH);
            int horseY = 220 + horses.indexOf(horse) * 60; // Atur Y berdasarkan nomor kuda
            g.setColor(Color.RED); // Gambar kuda dengan warna merah
            g.fillOval(horseX, horseY, 30, 30); // Gambar kuda sebagai lingkaran
            // Menampilkan jarak kuda
            g.setColor(Color.BLACK);
            g.drawString(horse.getDistance() + " m", horseX, horseY - 10); // Menampilkan jarak kuda di atasnya
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
