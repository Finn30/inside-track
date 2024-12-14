import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends JPanel {

    private double balance = 1000.0;  // User's starting balance
    private JLabel balanceLabel;
    private List<JRadioButton> horseRadioButtons = new ArrayList<>();
    private ButtonGroup horseButtonGroup = new ButtonGroup();
    private Horse selectedHorse;
    private JTextField betField;
    private Boolean isBetTerpasang = false;

    public LandingPage(JPanel mainPanel, GameplayPanel gameplayPanel) {
        setLayout(new BorderLayout());

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Selamat Datang di Game Pacuan Kuda!", JLabel.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        // Balance Label
        balanceLabel = new JLabel("Saldo Anda: Rp " + balance, JLabel.CENTER);
        add(balanceLabel, BorderLayout.NORTH);

        // Betting Panel (Input for betting)
        JPanel bettingPanel = new JPanel();
        bettingPanel.setLayout(new FlowLayout());

        JLabel betLabel = new JLabel("Masukkan jumlah taruhan: ");
        betField = new JTextField(10);  // Text field to input the bet amount
        JButton placeBetButton = new JButton("Pasang Taruhan");

        bettingPanel.add(betLabel);
        bettingPanel.add(betField);
        bettingPanel.add(placeBetButton);

        add(bettingPanel, BorderLayout.SOUTH);

        // Panel for selecting horses
        JPanel horseSelectionPanel = new JPanel();
        horseSelectionPanel.setLayout(new GridLayout(5, 1));

        // Menambahkan radio button untuk 5 kuda
        for (int i = 0; i < 5; i++) {
            final int index = i;  // Capture the value of 'i' for use in the lambda

            JRadioButton horseRadioButton = new JRadioButton("Kuda " + (i + 1));
            horseRadioButton.addActionListener(e -> selectedHorse = new Horse("Horse " + (index + 1)));  // Use 'index' instead of 'i'
            horseRadioButtons.add(horseRadioButton);
            horseButtonGroup.add(horseRadioButton);  // Add to ButtonGroup to ensure only one can be selected
            horseSelectionPanel.add(horseRadioButton);
        }

        add(horseSelectionPanel, BorderLayout.WEST);

        // Start button to begin the game
        JButton startButton = new JButton("Mulai");
        add(startButton, BorderLayout.CENTER);

        // ActionListener for the start button
        startButton.addActionListener(e -> {
            // Check if bet has been placed and if the selected horse is valid
            if (selectedHorse == null || betField.getText().isEmpty()|| isBetTerpasang == false) {
                JOptionPane.showMessageDialog(this, "Silakan pasang taruhan terlebih dahulu sebelum memulai balapan.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Prevent the game from starting if the bet is not placed
            }

            try {
                double betAmount = Double.parseDouble(betField.getText());
                if (betAmount <= 0 || betAmount > balance) {
                    JOptionPane.showMessageDialog(this, "Jumlah taruhan tidak valid atau saldo tidak cukup.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah taruhan yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset the game
            gameplayPanel.resetGame();

            // Switch to the gameplay panel
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Gameplay");

            // Start the background animation after the gameplay panel is displayed
            gameplayPanel.startAnimation(mainPanel);
        });

        // ActionListener for the place bet button
        placeBetButton.addActionListener(e -> {
            try {
                isBetTerpasang = true;
                double betAmount = Double.parseDouble(betField.getText());
                if (betAmount <= balance && betAmount > 0 && selectedHorse != null) {
                    // Update the balance after placing the bet
                    balance -= betAmount;
                    balanceLabel.setText("Saldo Anda: Rp " + balance);

                    // Pass the bet amount and selected horse to the gameplay panel
                    gameplayPanel.setBetAmount(betAmount);

                    // Optionally, you can display a confirmation message
                    JOptionPane.showMessageDialog(this, "Taruhan Anda sebesar Rp " + betAmount + " pada kuda: " + selectedHorse.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Jumlah taruhan tidak valid, saldo tidak cukup, atau belum memilih kuda.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah taruhan yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
