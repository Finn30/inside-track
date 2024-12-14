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

        // Atur tata letak
        setLayout(new BorderLayout());

        // Judul panel
        JLabel titleLabel = new JLabel("Hasil Balapan", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE); // Ubah warna teks agar kontras dengan background
        add(titleLabel, BorderLayout.NORTH);

        // Panel hasil balapan
        JPanel resultGrid = new JPanel(new GridBagLayout());
        resultGrid.setOpaque(false); // Transparan agar background terlihat
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Margin antar elemen
        gbc.gridy = 0;

        // Tambahkan kuda Juara 2 (Kiri)
        if (topFinishers.size() > 1) {
            gbc.gridx = 0; // Posisi kiri
            gbc.anchor = GridBagConstraints.CENTER;
            resultGrid.add(createHorsePanel(topFinishers.get(1), "Juara 2"), gbc);
        }

        // Tambahkan kuda Juara 1 (Tengah)
        if (topFinishers.size() > 0) {
            gbc.gridx = 1; // Posisi tengah
            gbc.anchor = GridBagConstraints.CENTER;
            resultGrid.add(createHorsePanel(topFinishers.get(0), "Juara 1"), gbc);
        }

        // Tambahkan kuda Juara 3 (Kanan)
        if (topFinishers.size() > 2) {
            gbc.gridx = 2; // Posisi kanan
            gbc.anchor = GridBagConstraints.CENTER;
            resultGrid.add(createHorsePanel(topFinishers.get(2), "Juara 3"), gbc);
        }

        add(resultGrid, BorderLayout.CENTER);

        // Tombol kembali
        JButton backButton = new JButton("Kembali ke Menu");
        backButton.addActionListener(e -> {
            // Reset gameplay
            Container parent = getParent();
            for (Component comp : parent.getComponents()) {
                if (comp instanceof GameplayPanel) {
                    ((GameplayPanel) comp).resetGame();
                }
            }

            // Tampilkan LandingPage
            CardLayout cl = (CardLayout) parent.getLayout();
            cl.show(parent, "Landing");
        });
        add(backButton, BorderLayout.SOUTH);
    }

    /**
     * Membuat panel individu untuk setiap kuda.
     */
    private JPanel createHorsePanel(Horse horse, String positionLabel) {
        JPanel horsePanel = new JPanel();
        horsePanel.setLayout(new BorderLayout());
        horsePanel.setOpaque(false); // Transparan agar background terlihat

        // Gambar kuda
        ImageIcon horseIcon = new ImageIcon(horse.getImageFolder() + "1.png");
        Image scaledImage = horseIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        horseIcon = new ImageIcon(scaledImage);

        JLabel horseImageLabel = new JLabel(horseIcon);
        horseImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        horsePanel.add(horseImageLabel, BorderLayout.CENTER);

        // Nama kuda dan posisi
        JLabel horseNameLabel = new JLabel(horse.getName(), JLabel.CENTER);
        horseNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        horseNameLabel.setForeground(Color.WHITE); // Ubah warna teks agar kontras dengan background

        JLabel positionLabelComponent = new JLabel(positionLabel, JLabel.CENTER);
        positionLabelComponent.setFont(new Font("Arial", Font.PLAIN, 16));
        positionLabelComponent.setForeground(Color.YELLOW); // Kontras dengan warna teks lainnya

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false); // Transparan
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