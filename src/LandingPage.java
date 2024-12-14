import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class LandingPage extends JPanel {

    private Clip backgroundMusicClip;
    private Player player;
    private JLabel pointsLabel;
    private String selectedHorse; // To store the selected horse

    public LandingPage(JPanel mainPanel, GameplayPanel gameplayPanel, Player player) {
        this.player = player;
        this.selectedHorse = null; // Initialize as no selection

        setLayout(new BorderLayout()); // Use BorderLayout for better layout management

        // Set background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("assets/img/background.jpg"));
        backgroundLabel.setLayout(new BoxLayout(backgroundLabel, BoxLayout.Y_AXIS)); // Overlay layout on background
        add(backgroundLabel, BorderLayout.CENTER);

        // Add spacer at the top to move Welcome label upwards
        backgroundLabel.add(Box.createVerticalStrut(20));

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("  Welcome to Inside Track!  ", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 56));
        welcomeLabel.setForeground(Color.BLACK); // Black text for contrast
        welcomeLabel.setOpaque(true); // Make the label opaque to show background color
        welcomeLabel.setBackground(new Color(255, 255, 255, 150)); // White background with some transparency
        welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
        backgroundLabel.add(welcomeLabel);

        // Add a larger spacer below Welcome label to move other components down
        backgroundLabel.add(Box.createVerticalStrut(50));

        // Create a panel for the horse selection with clickable images
        JPanel horseSelectionPanel = new JPanel();
        horseSelectionPanel.setLayout(new GridLayout(1, 5, 20, 20)); // 1 row, 5 columns, with spacing
        horseSelectionPanel.setOpaque(false); // Make it transparent
        horseSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Array of horse names and corresponding image file paths
        String[] horseNames = { "Horse Green", "Horse Pink", "Horse Purple", "Horse Red", "Horse Brown" };
        String[] horseImages = {
                "assets/horse-running/horse-green/1.png",
                "assets/horse-running/horse-pink/1.png",
                "assets/horse-running/horse-purple/1.png",
                "assets/horse-running/horse-red/1.png",
                "assets/horse-running/horse-brown/1.png"
        };

        for (int i = 0; i < horseNames.length; i++) {
            // Load the image and scale it up to a larger size (e.g., 200x200 pixels)
            ImageIcon horseImageIcon = new ImageIcon(horseImages[i]);
            Image horseImage = horseImageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            horseImageIcon = new ImageIcon(horseImage);

            // Create a panel to hold the horse image
            JPanel horsePanel = new JPanel();
            horsePanel.setLayout(new BorderLayout());
            horsePanel.setOpaque(false); // Transparent background
            horsePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 3), // White border
                    BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding inside border
            ));

            // Add the image to the panel
            JLabel horseImageLabel = new JLabel(horseImageIcon);
            horseImageLabel.setHorizontalAlignment(JLabel.CENTER);
            horsePanel.add(horseImageLabel, BorderLayout.CENTER);

            horseSelectionPanel.add(horsePanel);

            // Add mouse listener for selection
            int index = i; // Capture the current index for use inside the listener
            horsePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedHorse = horseNames[index];

                    // Highlight the selected horse
                    for (Component comp : horseSelectionPanel.getComponents()) {
                        if (comp instanceof JPanel) {
                            JPanel panel = (JPanel) comp;
                            panel.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(Color.WHITE, 3), // Default white border
                                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                        }
                    }
                    horsePanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.YELLOW, 5), // Highlighted yellow border
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                }
            });
        }

        // Add horse selection panel to main layout
        backgroundLabel.add(horseSelectionPanel);

        // Add a spacer below the horse selection panel to move the bet panel downward
        backgroundLabel.add(Box.createVerticalStrut(30));

        // Panel for placing the bet input
        JPanel betPanel = new JPanel();
        betPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Align components centrally
        betPanel.setOpaque(false); // Transparent background

        JLabel betLabel = new JLabel("Bet Points: ", JLabel.RIGHT);
        betLabel.setForeground(Color.WHITE);
        betPanel.add(betLabel);

        JTextField betInput = new JTextField(10);
        betPanel.add(betInput);

        pointsLabel = new JLabel("Points: " + player.getPoints());
        pointsLabel.setForeground(Color.WHITE);
        betPanel.add(pointsLabel);

        // Add bet panel
        backgroundLabel.add(betPanel);

        // Add a spacer below the bet panel to position the start button
        backgroundLabel.add(Box.createVerticalStrut(20));

        // Start game button
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(CENTER_ALIGNMENT); // Center the start button
        startButton.setPreferredSize(new Dimension(200, 50)); // Increase button size
        // Set the background color of the start button to black
        startButton.setBackground(Color.YELLOW);
        startButton.setForeground(Color.BLACK); // Set the text color to white for contrast
        backgroundLabel.add(startButton);

        startButton.addActionListener(e -> {
            if (selectedHorse == null) {
                JOptionPane.showMessageDialog(this, "Please select a horse!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int betPoints;
            try {
                betPoints = Integer.parseInt(betInput.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid bet amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (betPoints <= 0) {
                JOptionPane.showMessageDialog(this, "Bet amount must be greater than 0!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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

            // Set player bet
            gameplayPanel.setPlayerBet(selectedHorse, betPoints);

            // Start animation and music for gameplay
            gameplayPanel.startAnimation(mainPanel);
            gameplayPanel.playGameplayMusic();
        });

        // Play landing page music when LandingPage is displayed
        playBackgroundMusic("assets/music/music-landingpage.wav");
    }

    public void updatePointsDisplay() {
        pointsLabel.setText("Points: " + player.getPoints());
    }

    public void playBackgroundMusic(String musicFilePath) {
        Thread musicThread = new Thread(() -> {
            try {
                File musicFile = new File(musicFilePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);

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
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusicClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }
}
