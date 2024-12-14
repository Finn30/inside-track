import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class LandingPage extends JPanel {

    private Clip backgroundMusicClip;

    public LandingPage(JPanel mainPanel, GameplayPanel gameplayPanel) {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Selamat Datang di Game Pacuan Kuda!", JLabel.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Mulai");
        add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            // Hentikan musik LandingPage sebelum memulai game
            stopBackgroundMusic();

            // Reset game
            gameplayPanel.resetGame();

            // Ganti panel ke Gameplay
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Gameplay");

            // Mulai animasi background dan musik Gameplay setelah GameplayPanel ditampilkan
            gameplayPanel.startAnimation(mainPanel);
            gameplayPanel.playGameplayMusic(); // Memulai musik gameplay
        });

        // Memutar musik latar belakang
        playBackgroundMusic("assets/music/music-landingpage.wav");
    }

    // Method untuk memutar musik
    public void playBackgroundMusic(String musicFilePath) {
        Thread musicThread = new Thread(() -> {
            try {
                File musicFile = new File(musicFilePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);

                // Mengubah format audio ke 44.1 kHz, 16-bit stereo jika file menggunakan format
                // yang tidak didukung
                AudioFormat baseFormat = audioStream.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100, // Sample rate
                        16, // Bit depth
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2, // Frame size (2 bytes per channel, hence *2)
                        44100,
                        false // Little endian
                );

                AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);

                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(decodedAudioStream);
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Musik berulang
                backgroundMusicClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    // Method untuk menghentikan musik saat diperlukan
    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }

}