import java.util.ArrayList;
import java.util.List;

public class Race {

    public static int RACE_LENGTH = 20000;
    public static int NUMBER_OF_RUNNERS = 5;
    private int DELAY = 25;
    private List<Horse> runners = new ArrayList<Horse>();
    private RaceListener listener;
    private String bettingHorse; // The selected horse for betting

    public Race() {
        for (int i = 0; i < NUMBER_OF_RUNNERS; i++) {
            runners.add(new Horse("Horse " + (i + 1)));
        }
    }

    public void setListener(RaceListener listener) {
        this.listener = listener;
    }

    // Set the selected horse for betting
    public void setBettingHorse(String horseName) {
        this.bettingHorse = horseName;
    }

    // Get the selected horse for betting
    public String getBettingHorse() {
        return this.bettingHorse;
    }

    public Horse race() {
        Horse winner = getWinner();
        while (winner == null) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    public List<Horse> getTopFinishers() {
        runners.sort((h1, h2) -> Integer.compare(h2.getDistance(), h1.getDistance()));
        return runners.subList(0, Math.min(3, runners.size())); // Get top 3 horses
    }

    public String getBettingHorseName() {
        return this.bettingHorse; // Return the betting horse
    }
}
