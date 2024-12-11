import java.awt.Graphics;
import java.awt.Image;
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
        ImageIcon icon = new ImageIcon("assets/img/background2.jpg");
        backgroundImage = icon.getImage();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            // Gambar background bergerak
            int backgroundWidth = backgroundImage.getWidth(this);
            int backgroundHeight = backgroundImage.getHeight(this);

            // Gambar background hanya sekali tanpa loop
            g.drawImage(backgroundImage, xPosition, 0, backgroundWidth, backgroundHeight, this);
        }

        // Looping gambar kuda
        if (!raceFinished) {
            int frameCount = 12; // Jumlah frame gambar kuda
            int frameIndex = (int) ((System.currentTimeMillis() / 50) % frameCount); // Hitung frame index berdasarkan
                                                                                     // waktu

            for (Horse horse : horses) {
                int horseX = (int) (horse.getDistance() * getWidth() / Race.RACE_LENGTH);
                int horseY = 80 + horses.indexOf(horse) * 80; // Atur Y berdasarkan nomor kuda

                // Load gambar kuda berdasarkan frame index
                ImageIcon horseIcon = new ImageIcon("./assets/horse-running/horse/" + (frameIndex + 1) + ".png");
                Image horseImage = horseIcon.getImage();

                // Gambar kuda sebagai gambar
                g.drawImage(horseImage, horseX, horseY, 200, 200, null);

                // Hentikan loop jika kuda pertama sudah mencapai akhir frame
                if (horses.get(0).getDistance() >= Race.RACE_LENGTH) {
                    raceFinished = true;
                }
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
            }

            // Repaint panel untuk memperbarui tampilan
            repaint();

            // Delay untuk mengontrol kecepatan animasi
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
