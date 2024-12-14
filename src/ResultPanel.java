import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    private Image backgroundImage;

    public ResultPanel(List<Horse> topFinishers, String backgroundImagePath, GameplayPanel gameplayPanel) {
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
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // Gunakan BoxLayout untuk menambahkan
                                                                             // elemen secara vertikal
        bottomPanel.setOpaque(false);
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the panel in the center

        // Panel untuk hasil taruhan
        JPanel betResultPanel = new JPanel();
        betResultPanel.setLayout(new BoxLayout(betResultPanel, BoxLayout.Y_AXIS)); // Menambahkan label secara vertikal
        betResultPanel.setOpaque(false);

        JLabel betResultLabel = new JLabel(
                isWin
                        ? "Congratulations! You win " + (playerBetPoints * 2) + " points!"
                        : "You lose! Points deducted: " + playerBetPoints,
                JLabel.CENTER);
        betResultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        betResultLabel.setForeground(isWin ? Color.GREEN : Color.RED);

        JLabel pointsLabel = new JLabel("Your current points: " + player.getPoints(), JLabel.CENTER);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        pointsLabel.setForeground(Color.WHITE);

        betResultPanel.add(betResultLabel);
        betResultPanel.add(Box.createVerticalStrut(10)); // Menambahkan jarak antara label
        betResultPanel.add(pointsLabel);

        bottomPanel.add(betResultPanel); // Menambahkan betResultPanel ke bottomPanel

        // Tombol kembali (BET AGAIN)
        JButton backButton = new JButton("BET AGAIN");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setOpaque(true);

        backButton.addActionListener(e -> {
            // Reset gameplay dan kembali ke LandingPage
            gameplayPanel.resetGame();

            // Ganti musik ke lagu landing page
            gameplayPanel.stopGameplayMusic();
            // gameplayPanel.playBackgroundMusic("assets/music/music-landingpage.wav"); //
            // Putar ulang musik landing page

            Container parent = getParent();
            CardLayout cl = (CardLayout) parent.getLayout();
            cl.show(parent, "Landing");
        });
        bottomPanel.add(Box.createVerticalStrut(10)); // Menambahkan jarak antara betResultPanel dan tombol
        bottomPanel.add(backButton); // Menambahkan tombol "BET AGAIN" ke bottomPanel

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