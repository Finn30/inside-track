import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    public ResultPanel(List<Horse> topFinishers) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Hasil Balapan", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel resultGrid = new JPanel(new GridLayout(1, 3, 10, 10));
        for (int i = 0; i < topFinishers.size(); i++) {
            Horse horse = topFinishers.get(i);

            // Panel individu untuk setiap juara
            JPanel horsePanel = new JPanel();
            horsePanel.setLayout(new BorderLayout());

            // Gambar kuda
            ImageIcon horseIcon = new ImageIcon(horse.getImageFolder());
            JLabel horseImageLabel = new JLabel(horseIcon);
            horseImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            horsePanel.add(horseImageLabel, BorderLayout.CENTER);

            // Nama kuda dan posisinya
            JLabel horseNameLabel = new JLabel(horse.getName(), JLabel.CENTER);
            horseNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
            JLabel positionLabel = new JLabel(getPositionLabel(i), JLabel.CENTER);
            positionLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.add(horseNameLabel);
            textPanel.add(positionLabel);

            horsePanel.add(textPanel, BorderLayout.SOUTH);
            resultGrid.add(horsePanel);
        }

        add(resultGrid, BorderLayout.CENTER);

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

    private String getPositionLabel(int index) {
        switch (index) {
            case 0:
                return "Juara 1";
            case 1:
                return "Juara 2";
            case 2:
                return "Juara 3";
            default:
                return "";
        }
    }
}