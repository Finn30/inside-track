import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    public ResultPanel(List<Horse> topFinishers) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Hasil Balapan: Juara 1, 2, dan 3", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        StringBuilder results = new StringBuilder("Hasil Balapan:\n");
        for (int i = 0; i < topFinishers.size(); i++) {
            results.append("Juara ").append(i + 1).append(": ").append(topFinishers.get(i).getName()).append("\n");
        }
        resultsArea.setText(results.toString());
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);

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
}