import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.List;

public class GameplayPanel extends JPanel implements Runnable, RaceListener {
    private JPanel mainPanel;
    private Image backgroundImage;
    private int xPosition; // Posisi X gambar
    private int moveSpeed = 3; // Kecepatan pergerakan gambar
    private boolean startAnimation = false; // Flag untuk memulai animasi
    private boolean raceFinished = false; // Flag untuk mengetahui apakah balapan selesai

    private Race race; // Referensi ke objek Race
    private List<Horse> horses; // List kuda
    private JLabel statusLabel;

    public GameplayPanel(JPanel mainPanel, Race race) {
        this.mainPanel = mainPanel;
        this.race = race;
        this.horses = race.getRunners(); // Ambil daftar kuda
        this.race.setListener(this); // Set panel ini sebagai listener

        // Load gambar background
        ImageIcon icon = new ImageIcon("assets/img/background2.jpg");
        backgroundImage = icon.getImage();

        // Create status label to show winner
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setForeground(Color.RED);
        statusLabel.setPreferredSize(new Dimension(800, 50));
        add(statusLabel, BorderLayout.NORTH);
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
    public void startAnimation(JPanel mainPanel) {
        if (!startAnimation) {
            startAnimation = true;
            Thread animationThread = new Thread(this);
            animationThread.start(); // Mulai animasi latar belakang
            Thread raceThread = new Thread(() -> {
                race.race(); // Mulai balapan dan cari pemenangnya
                raceFinished = true; // Tandai balapan selesai
                List<Horse> topFinishers = race.getTopFinishers(); // Ambil pemenang balapan

                SwingUtilities.invokeLater(() -> {
                    ResultPanel resultPanel = new ResultPanel(topFinishers);
                    mainPanel.add(resultPanel, "Result");
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "Result");
                });
                // repaint(); // Repaint panel setelah balapan selesai
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

    public void notifyRaceProgress() {
    }

    public void notifyStatus(String status) {
        statusLabel.setText(status);

        JOptionPane.showMessageDialog(this, "Kuda yang menang: " + status, "Pemenang Balapan",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetGame() {
        raceFinished = false;
        startAnimation = false;
        xPosition = 0;
        for (Horse horse : horses) {
            horse.reset(); // Reset posisi kuda
        }
        repaint(); // Perbarui tampilan
    }

}
