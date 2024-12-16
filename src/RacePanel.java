import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RacePanel extends JPanel {

    // Declare all the images (12 images in total)
    BufferedImage img1 = null;
    BufferedImage img2 = null;
    BufferedImage img3 = null;
    BufferedImage img4 = null;
    BufferedImage img5 = null;
    BufferedImage img6 = null;
    BufferedImage img7 = null;
    BufferedImage img8 = null;
    BufferedImage img9 = null;
    BufferedImage img10 = null;
    BufferedImage img11 = null;
    BufferedImage img12 = null;

    private Race race;
    private int counter;
    private JLabel statusLabel;

    public RacePanel(Race race) {
        super();
        this.race = race;

        try {
            // Load all 12 images
            img1 = ImageIO.read(new File("1.png"));
            img2 = ImageIO.read(new File("2.png"));
            img3 = ImageIO.read(new File("3.png"));
            img4 = ImageIO.read(new File("4.png"));
            img5 = ImageIO.read(new File("5.png"));
            img6 = ImageIO.read(new File("6.png"));
            img7 = ImageIO.read(new File("7.png"));
            img8 = ImageIO.read(new File("8.png"));
            img9 = ImageIO.read(new File("9.png"));
            img10 = ImageIO.read(new File("10.png"));
            img11 = ImageIO.read(new File("11.png"));
            img12 = ImageIO.read(new File("12.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create status label to show winner
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setForeground(Color.RED);
        statusLabel.setPreferredSize(new Dimension(800, 50));
        add(statusLabel, BorderLayout.NORTH);
    }

    public void notifyRaceProgress() {

    }

    public void notifyStatus(String status) {
        statusLabel.setText(status);

        JOptionPane.showMessageDialog(this, "Kuda yang menang: " + status, "Pemenang Balapan",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to get the image based on the animation step
    public BufferedImage getImage(int step) {
        switch (step) {
            case 0:
                return img1;
            case 1:
                return img2;
            case 2:
                return img3;
            case 3:
                return img4;
            case 4:
                return img5;
            case 5:
                return img6;
            case 6:
                return img7;
            case 7:
                return img8;
            case 8:
                return img9;
            case 9:
                return img10;
            case 10:
                return img11;
            case 11:
                return img12;
            default:
                return img1; // Default image
        }
    }

    // Increment the counter for animation steps
    public void stepAnimation() {
        counter++;
        repaint(); // Trigger a repaint to animate
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Call superclass's paint method to ensure proper rendering

        // Determine animation step based on counter, using modulo 12 for 12 images
        int animStep = counter % 12;
        int laneHeight = this.getHeight() / Race.NUMBER_OF_RUNNERS;
        int laneWidth = this.getWidth() - img1.getWidth(); // Space available for movement
        int y = 0;

        // Draw each runner's progress on the race track
        for (int i = 0; i < Race.NUMBER_OF_RUNNERS; i++) {
            int x = (int) (race.getRunners().get(i).getRaceComplete() * laneWidth);
            BufferedImage horseImage = getImage(animStep); // Use getImage method
            g.drawImage(horseImage, x, y, horseImage.getWidth(), laneHeight, null);
            y += laneHeight;
        }
    }
}
