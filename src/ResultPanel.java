import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    private Image backgroundImage;
    private JPanel mainPanel;

    public ResultPanel(List<Horse> topFinishers, String backgroundImagePath, GameplayPanel gameplayPanel) {
        this.mainPanel = mainPanel;
        // Set background image
        try {
            backgroundImage = new ImageIcon(backgroundImagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ambil data pemain dan taruhan
        Player player = gameplayPanel.getPlayer();
        String playerBetHorse = gameplayPanel.getPlayerBetHorse();
        int playerBetPoints = gameplayPanel.getPlayerBetPoints();

        // Logika menang/kalah
        Horse winner = topFinishers.get(0); // Juara 1
        boolean isWin = winner.getName().equals(playerBetHorse);
        int pointsChange = isWin ? playerBetPoints * 2 : -playerBetPoints;

        // Perbarui poin pemain
        player.addPoints(pointsChange);

        // Atur tata letak
        setLayout(new BorderLayout());

        // Judul panel
        JLabel titleLabel = new JLabel("Race Results", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // Panel hasil balapan (juara)
        JPanel resultGrid = new JPanel(new GridBagLayout());
        resultGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridy = 0;

        // Tambahkan kuda Juara 2 (Kiri)
        if (topFinishers.size() > 1) {
            gbc.gridx = 0;
            resultGrid.add(createHorsePanel(topFinishers.get(1), "2ND"), gbc);
        }

        // Tambahkan kuda Juara 1 (Tengah)
        if (topFinishers.size() > 0) {
            gbc.gridx = 1;
            resultGrid.add(createHorsePanel(topFinishers.get(0), "1ST"), gbc);
        }

        // Tambahkan kuda Juara 3 (Kanan)
        if (topFinishers.size() > 2) {
            gbc.gridx = 2;
            resultGrid.add(createHorsePanel(topFinishers.get(2), "3RD"), gbc);
        }

        add(resultGrid, BorderLayout.CENTER); // Tempatkan di tengah panel

        // Panel untuk hasil taruhan (betResultPanel) dan tombol "BET AGAIN"
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // Tata letak vertikal
        bottomPanel.setOpaque(false);
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Semua elemen akan rata tengah

        // 1. Tulisan hasil taruhan (betResultLabel)
        JLabel betResultLabel = new JLabel(
                isWin
                        ? "Congratulations! You win " + (playerBetPoints * 2) + " points!"
                        : "You lose! Points deducted: " + playerBetPoints,
                JLabel.CENTER);
        betResultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        betResultLabel.setForeground(isWin ? Color.GREEN : Color.RED);
        betResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Rata tengah

        // 2. Tulisan "Your current points"
        JLabel pointsLabel = new JLabel("Your current points: " + player.getPoints(), JLabel.CENTER);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Rata tengah

        // 3. Tombol "BET AGAIN"
        JButton backButton = new JButton("BET AGAIN");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setOpaque(true);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Rata tengah

        backButton.addActionListener(e -> {
            // Reset gameplay and return to LandingPage
            gameplayPanel.resetGame();

            // Stop current gameplay music
            gameplayPanel.stopGameplayMusic();

            // Update points on the LandingPage
            Container parent = getParent();

            if (parent instanceof JPanel) {
                JPanel mainPanel = (JPanel) parent;

                // Retrieve LandingPage instance
                Component landingPageComponent = mainPanel.getComponent(0); // Assuming LandingPage is the first
                                                                            // component
                if (landingPageComponent instanceof LandingPage) {
                    LandingPage landingPage = (LandingPage) landingPageComponent;

                    // Update points display on LandingPage
                    landingPage.updatePointsDisplay();

                    // Restart landing page music
                    landingPage.playBackgroundMusic("assets/music/music-landingpage.wav");
                }

                // Switch to LandingPage using CardLayout
                CardLayout cl = (CardLayout) parent.getLayout();
                cl.show(parent, "Landing");
            }
        });

        // Menambahkan elemen ke bottomPanel secara vertikal
        bottomPanel.add(Box.createVerticalStrut(10)); // Jarak atas
        bottomPanel.add(betResultLabel); // Tulisan hasil taruhan
        bottomPanel.add(Box.createVerticalStrut(10)); // Jarak antar komponen
        bottomPanel.add(pointsLabel); // Tulisan "Your current points"
        bottomPanel.add(Box.createVerticalStrut(20)); // Jarak antara tulisan dan tombol
        bottomPanel.add(backButton); // Tombol BET AGAIN
        bottomPanel.add(Box.createVerticalStrut(10)); // Jarak bawah

        add(bottomPanel, BorderLayout.SOUTH); // Menempatkan bottomPanel di bagian bawah

    }

    /**
     * Membuat panel individu untuk setiap kuda.
     */
    private JPanel createHorsePanel(Horse horse, String positionLabel) {
        JPanel horsePanel = new JPanel(new BorderLayout());
        horsePanel.setOpaque(false);

        ImageIcon horseIcon = new ImageIcon(horse.getImageFolder() + "1.png");
        Image scaledImage = horseIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        horseIcon = new ImageIcon(scaledImage);

        JLabel horseImageLabel = new JLabel(horseIcon);
        horseImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        horsePanel.add(horseImageLabel, BorderLayout.CENTER);

        JLabel horseNameLabel = new JLabel(horse.getName(), JLabel.CENTER);
        horseNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        horseNameLabel.setForeground(Color.WHITE);

        JLabel positionLabelComponent = new JLabel(positionLabel, JLabel.CENTER);
        positionLabelComponent.setFont(new Font("Arial", Font.PLAIN, 16));
        positionLabelComponent.setForeground(Color.YELLOW);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(horseNameLabel);
        textPanel.add(positionLabelComponent);

        horsePanel.add(textPanel, BorderLayout.SOUTH);

        return horsePanel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
