import java.util.ArrayList;
import java.util.List;

public class Race {

	public static int RACE_LENGTH = 20000;
	public static int NUMBER_OF_RUNNERS = 5;
	private int DELAY = 25;
	private List<Horse> runners = new ArrayList<Horse>();
	private RaceListener listener;

	public Race() {
		for (int i = 0; i < NUMBER_OF_RUNNERS; i++) {
			runners.add(new Horse("Horse " + (i + 1)));
		}
	}

	public void setListener(RaceListener listener) {
		this.listener = listener;
	}

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
				listener.notifyRaceProgress(); // Notify race progress (optional)
			}
			winner = getWinner();
		}

		// Notify when the race is finished and the winner is found
		// if (listener != null && winner != null) {
		// listener.notifyStatus(winner.getName() + " Wins!");
		// }

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

	public List<Horse> getTopFinishers() {
		// Urutkan berdasarkan distance dalam urutan menurun
		runners.sort((h1, h2) -> Integer.compare(h2.getDistance(), h1.getDistance()));
		return runners.subList(0, Math.min(3, runners.size())); // Ambil 3 kuda teratas
	}

}
