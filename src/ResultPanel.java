import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {

    private Image backgroundImage;

    public ResultPanel(List<Horse> topFinishers, String backgroundImagePath) {

        // Set background image
        try {
            backgroundImage = new ImageIcon(backgroundImagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        // Judul di bagian atas
        JLabel titleLabel = new JLabel("Hasil Balapan", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Grid dengan 3 kolom: Juara 2 - Juara 1 - Juara 3
        JPanel resultGrid = new JPanel(new GridLayout(1, 3, 10, 10));

        // Tambahkan panel untuk juara 2
        if (topFinishers.size() > 1) {
            resultGrid.add(createHorsePanel(topFinishers.get(1), "Juara 2"));
        }

        // Tambahkan panel untuk juara 1
        if (topFinishers.size() > 0) {
            resultGrid.add(createHorsePanel(topFinishers.get(0), "Juara 1"));
        }

        // Tambahkan panel untuk juara 3
        if (topFinishers.size() > 2) {
            resultGrid.add(createHorsePanel(topFinishers.get(2), "Juara 3"));
        }

        add(resultGrid, BorderLayout.CENTER);

        // Tombol kembali ke menu
        JButton backButton = new JButton("Kembali ke Menu");
        backButton.addActionListener(e -> {
            // Reset gameplay
            Container parent = getParent();
            for (Component comp : parent.getComponents()) {
                if (comp instanceof GameplayPanel) {
                    ((GameplayPanel) comp).resetGame();
                    ((GameplayPanel) comp).stopGameplayMusic();
                }
            }

            for (Component comp : parent.getComponents()) {
                if (comp instanceof LandingPage) {
                    ((LandingPage) comp).playBackgroundMusic("assets/music/music-landingpage.wav"); // Putar ulang musik
                }
            }

            // Tampilkan halaman utama
            CardLayout cl = (CardLayout) parent.getLayout();
            cl.show(parent, "Landing");
        });
        add(backButton, BorderLayout.SOUTH);
    }

    // Fungsi untuk membuat panel individu untuk setiap kuda
    private JPanel createHorsePanel(Horse horse, String positionLabel) {
        JPanel horsePanel = new JPanel();
        horsePanel.setLayout(new BorderLayout());

        // Gambar kuda
        ImageIcon horseIcon = new ImageIcon(horse.getImageFolder() + "1.png");
        JLabel horseImageLabel = new JLabel(horseIcon);
        horseImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        horsePanel.add(horseImageLabel, BorderLayout.CENTER);

        // Nama kuda dan posisinya
        JLabel horseNameLabel = new JLabel(horse.getName(), JLabel.CENTER);
        horseNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel position = new JLabel(positionLabel, JLabel.CENTER);
        position.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(horseNameLabel);
        textPanel.add(position);

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
