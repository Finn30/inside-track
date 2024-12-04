import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RacePanel extends JPanel {

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

	public RacePanel(Race race) {
		super();
		this.race = race;
		try {
			img1 = ImageIO.read(new File("assets/horse/1.png"));
			img2 = ImageIO.read(new File("assets/horse/2.png"));
			img3 = ImageIO.read(new File("assets/horse/3.png"));
			img4 = ImageIO.read(new File("assets/horse/4.png"));
			img5 = ImageIO.read(new File("assets/horse/5.png"));
			img6 = ImageIO.read(new File("assets/horse/6.png"));
			img7 = ImageIO.read(new File("assets/horse/7.png"));
			img8 = ImageIO.read(new File("assets/horse/8.png"));
			img9 = ImageIO.read(new File("assets/horse/9.png"));
			img10 = ImageIO.read(new File("assets/horse/10.png"));
			img11 = ImageIO.read(new File("assets/horse/11.png"));
			img12 = ImageIO.read(new File("assets/horse/12.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stepAnimation() {
		counter++;
	}

	public void paint(Graphics g) {

		int animStep = counter % 3;

		int laneHeight = this.getHeight() / Race.NUMBER_OF_RUNNERS;
		int laneWidth = this.getWidth() - img1.getWidth();

		int y = 0;

		for (int i = 0; i < Race.NUMBER_OF_RUNNERS; i++) {
			int x = (int) (race.getRunners().get(i).getRaceComplete() * laneWidth);
			if (animStep == 0) {
				g.drawImage(img1, x, y, img1.getWidth(), laneHeight, null);
			}
			if (animStep == 1) {
				g.drawImage(img2, x, y, img2.getWidth(), laneHeight, null);
			}
			if (animStep == 2) {
				g.drawImage(img3, x, y, img3.getWidth(), laneHeight, null);
			}
			y += laneHeight;
		}
	}

}