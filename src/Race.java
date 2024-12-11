import java.util.ArrayList;
import java.util.List;

public class Race {

	public static int RACE_LENGTH = 20000;

	public static int NUMBER_OF_RUNNERS = 5;

	private int DELAY = 25;

	private List<Horse> runners = new ArrayList<Horse>();

	private RaceListener listener;

	/**
	 * Construct the race object and add the runners to the array
	 * of horses.
	 */
	public Race() {
		for (int i = 0; i < NUMBER_OF_RUNNERS; i++) {
			runners.add(new Horse(String.valueOf(i)));
		}
	}

	/**
	 * Reset the race back to the start.
	 */
	public void reset() {
		for (Horse horse : runners) {
			horse.reset();
		}
	}

	/**
	 * Run the race and return the winner.
	 * 
	 * @return
	 */
	public Horse race() {

		Horse winner = getWinner();
		while (winner == null) {

			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
			}

			for (Horse runner : runners) {
				runner.run();
			}

			if (listener != null) {
				listener.notifyRaceProgress();
			}

			winner = getWinner();
		}
		return winner;

	}

	public Horse getWinner() {
		for (Horse horse : runners) {
			if (horse.getDistance() > RACE_LENGTH) {
				return horse;
			}
		}
		return null;
	}

	public List<Horse> getRunners() {
		return runners;
	}

	public void setListener(RaceListener listener) {
		this.listener = listener;
	}

}
