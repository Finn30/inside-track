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
    private Player player;
    private JLabel pointsLabel; // Add pointsLabel instance variable

    public LandingPage(JPanel mainPanel, GameplayPanel gameplayPanel, Player player) {
        this.player = player;
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Inside Track!", JLabel.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        JComboBox<String> horseSelection = new JComboBox<>(
                new String[] { "Horse Green", "Horse Pink", "Horse Purple", "Horse Red", "Horse Brown" });
        JTextField betInput = new JTextField(10);
        pointsLabel = new JLabel("Points: " + player.getPoints()); // Initialize the pointsLabel here

        JPanel selectionPanel = new JPanel();
        selectionPanel.add(new JLabel("Select Horse: "));
        selectionPanel.add(horseSelection);
        selectionPanel.add(new JLabel("Bet Points: "));
        selectionPanel.add(betInput);
        selectionPanel.add(pointsLabel);
        add(selectionPanel, BorderLayout.NORTH);

        JButton startButton = new JButton("Start Game");
        add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            String selectedHorse = (String) horseSelection.getSelectedItem();
            int betPoints;

            try {
                betPoints = Integer.parseInt(betInput.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid bet amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi: pastikan nilai taruhan > 0
            if (betPoints <= 0) {
                JOptionPane.showMessageDialog(this, "Bet amount must be greater than 0!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi: pastikan poin pemain mencukupi
            if (betPoints > player.getPoints()) {
                JOptionPane.showMessageDialog(this, "Not enough points!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Stop current background music
            stopBackgroundMusic();

            // Reset game
            gameplayPanel.resetGame();

            // Change panel to Gameplay
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Gameplay");

            // Set taruhan pemain
            gameplayPanel.setPlayerBet(selectedHorse, betPoints);

            // Start background animation and music for gameplay
            gameplayPanel.startAnimation(mainPanel);
            gameplayPanel.playGameplayMusic(); // Start gameplay music
        });

        // Play landing page music when LandingPage is displayed
        playBackgroundMusic("assets/music/music-landingpage.wav");
    }

    // Method to update points display when returning to LandingPage
    public void updatePointsDisplay() {
        pointsLabel.setText("Points: " + player.getPoints()); // Update the points label with current points
    }

    // Method to play background music
    public void playBackgroundMusic(String musicFilePath) {
        Thread musicThread = new Thread(() -> {
            try {
                File musicFile = new File(musicFilePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);

                // Decode the audio format if necessary
                AudioFormat baseFormat = audioStream.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100, // Sample rate
                        16, // Bit depth
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2, // Frame size (2 bytes per channel)
                        44100,
                        false // Little endian
                );

                AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);

                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(decodedAudioStream);
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
                backgroundMusicClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    // Method to stop the background music
    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }
}