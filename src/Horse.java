public class Horse extends AbstractHorse implements HorseInterface {
	public Horse(String name, String imageFolder) {
		super(name, imageFolder);
	}

	@Override
	public void run() {
		int step = (int) (Math.random() * 200);
		distance += step;
		distance += handicap;
	}

	@Override
	public double getRaceComplete() {
		return (double) distance / 5000;
	}
}