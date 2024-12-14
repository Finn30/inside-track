import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    private JLabel resultLabel;

    // Konstruktor menerima List<Horse> dan menampilkan hasil balapan
    public ResultPanel(List<Horse> topFinishers) {
        setLayout(new BorderLayout());

        // Label untuk menampilkan hasil
        resultLabel = new JLabel("Hasil Balapan:", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setPreferredSize(new Dimension(800, 50));
        add(resultLabel, BorderLayout.NORTH);

        // Area untuk menampilkan kuda yang menang
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        StringBuilder resultText = new StringBuilder("Hasil Balapan:\n");

        // Menampilkan 3 besar kuda yang menang
        for (int i = 0; i < topFinishers.size(); i++) {
            resultText.append("Juara ").append(i + 1).append(": ").append(topFinishers.get(i).getName()).append("\n");
        }

        resultArea.setText(resultText.toString());
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        // Button untuk kembali ke menu utama
        JButton backButton = new JButton("Kembali ke Menu");
        backButton.addActionListener(e -> {
            // Kembali ke halaman utama atau menu
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Landing");
        });

        add(backButton, BorderLayout.SOUTH);
    }
}
