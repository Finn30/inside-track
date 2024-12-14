import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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

    private Clip gameplayMusicClip; // Clip untuk memutar musik gameplay

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

    // Method untuk memutar musik gameplay
    public void playGameplayMusic() {
        Thread musicThread = new Thread(() -> {
            try {
                File musicFile = new File("assets/music/music-race.wav"); // Path ke musik gameplay
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);

                AudioFormat baseFormat = audioStream.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        44100,
                        false);

                AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);

                gameplayMusicClip = AudioSystem.getClip();
                gameplayMusicClip.open(decodedAudioStream);
                gameplayMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Musik akan berulang
                gameplayMusicClip.start(); // Mulai musik gameplay
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    // Method untuk menghentikan musik gameplay jika diperlukan
    public void stopGameplayMusic() {
        if (gameplayMusicClip != null && gameplayMusicClip.isRunning()) {
            gameplayMusicClip.stop();
            gameplayMusicClip.close();
        }
    }

    @Override
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
                String imagePath = horse.getImageFolder() + (frameIndex + 1) + ".png";
                ImageIcon horseIcon = new ImageIcon(imagePath);
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

            // Memulai balapan di thread yang berbeda
            Thread raceThread = new Thread(() -> {
                race.race(); // Mulai balapan
                raceFinished = true; // Tandai balapan selesai
                List<Horse> topFinishers = race.getTopFinishers(); // Ambil pemenang

                SwingUtilities.invokeLater(() -> {
                    ResultPanel resultPanel = new ResultPanel(topFinishers, "assets/img/background2.jpg");
                    mainPanel.add(resultPanel, "Result");
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "Result");
                });
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
