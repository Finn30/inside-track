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
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends JPanel {

    private Clip backgroundMusicClip;

    private double balance = 1000.0; // User's starting balance
    private JLabel balanceLabel;
    private List<JRadioButton> horseRadioButtons = new ArrayList<>();
    private ButtonGroup horseButtonGroup = new ButtonGroup();
    private Horse selectedHorse;
    private JTextField betField;
    private Boolean isBetTerpasang = false;

    // Constructor now accepts a Race object
    public LandingPage(JPanel mainPanel, GameplayPanel gameplayPanel, Race race) {
        setLayout(new BorderLayout());

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Selamat Datang di Game Pacuan Kuda!", JLabel.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        // Balance Label
        balanceLabel = new JLabel("Saldo Anda: Rp " + balance, JLabel.CENTER);
        add(balanceLabel, BorderLayout.NORTH);

        // Betting Panel (Input for betting)
        JPanel bettingPanel = new JPanel();
        bettingPanel.setLayout(new FlowLayout());

        JLabel betLabel = new JLabel("Masukkan jumlah taruhan: ");
        betField = new JTextField(10); // Text field to input the bet amount
        JButton placeBetButton = new JButton("Pasang Taruhan");

        bettingPanel.add(betLabel);
        bettingPanel.add(betField);
        bettingPanel.add(placeBetButton);

        add(bettingPanel, BorderLayout.SOUTH);

        // Panel for selecting horses
        JPanel horseSelectionPanel = new JPanel();
        horseSelectionPanel.setLayout(new GridLayout(race.getRunners().size(), 1)); // Dynamically set the number of
                                                                                    // horses

        // Menambahkan radio button untuk setiap kuda dari race object
        for (Horse horse : race.getRunners()) {
            JRadioButton horseRadioButton = new JRadioButton(horse.getName()); // Use the horse's name
            horseRadioButton.addActionListener(e -> selectedHorse = horse); // Assign the selected horse

            horseRadioButtons.add(horseRadioButton);
            horseButtonGroup.add(horseRadioButton); // Add to ButtonGroup to ensure only one can be selected
            horseSelectionPanel.add(horseRadioButton);
        }

        add(horseSelectionPanel, BorderLayout.WEST);

        // Start button to begin the game
        JButton startButton = new JButton("Mulai");
        add(startButton, BorderLayout.CENTER);

        // ActionListener for the start button
        startButton.addActionListener(e -> {
            // Hentikan musik LandingPage sebelum memulai game
            stopBackgroundMusic();

            // Reset game
            gameplayPanel.resetGame();

            // Check if bet has been placed and if the selected horse is valid
            if (selectedHorse == null || betField.getText().isEmpty() || isBetTerpasang == false && balance == 0) {
                JOptionPane.showMessageDialog(this, "Silakan pasang taruhan terlebih dahulu sebelum memulai balapan.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return; // Prevent the game from starting if the bet is not placed
            }
            // Reset the game
            gameplayPanel.resetGame();

            // Switch to the gameplay panel
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Gameplay");

            gameplayPanel.startAnimation(mainPanel);
            gameplayPanel.playGameplayMusic();

            isBetTerpasang = false;
            betField.setText("");
        });

        playBackgroundMusic("assets/music/music-landingpage.wav");

        // ActionListener untuk tombol pasang taruhan
        placeBetButton.addActionListener(e -> {
            try {
                double betAmount = Double.parseDouble(betField.getText());

                // Validasi jumlah taruhan
                if (betAmount <= 0 || betAmount > balance) {
                    JOptionPane.showMessageDialog(this, "Jumlah taruhan tidak valid atau saldo tidak cukup.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validasi apakah kuda telah dipilih
                if (selectedHorse == null) {
                    JOptionPane.showMessageDialog(this, "Silakan pilih kuda terlebih dahulu.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Set taruhan jika valid
                isBetTerpasang = true;

                // Update saldo setelah taruhan
                balance -= betAmount;
                balanceLabel.setText("Saldo Anda: Rp " + balance);

                // Kirim jumlah taruhan dan kuda yang dipilih ke panel gameplay
                gameplayPanel.setBetAmount(betAmount);
                gameplayPanel.setSelectedHorse(selectedHorse);

                // Tampilkan pesan konfirmasi
                JOptionPane.showMessageDialog(this,
                        "Taruhan Anda sebesar Rp " + betAmount + " pada kuda: " + selectedHorse.getName());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah taruhan yang valid.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
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
