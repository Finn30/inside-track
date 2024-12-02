import javax.swing.*;
import java.awt.*;

public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel() {
        setLayout(new BorderLayout());
        JLabel leaderboardLabel = new JLabel("Leaderboard", JLabel.CENTER);
        add(leaderboardLabel, BorderLayout.NORTH);

        // Tempatkan data leaderboard
        JTextArea leaderboardData = new JTextArea("Data leaderboard akan ditampilkan di sini.");
        leaderboardData.setEditable(false);
        add(new JScrollPane(leaderboardData), BorderLayout.CENTER);
    }
}