public class Horse {

	private int distance;
	private int handicap;
	
	private String name;
	
	/**
	 * Constructor.
	 */
	public Horse(String name) {
		this.name = name;
	}
	
	/**
	 * Make one timesteps worth of run forwards along the track.
	 */
	public void run() {
		int step = (int)(Math.random() * 100);
		distance += step;
		distance += handicap;
	}
	
	/**
	 * Reset the position of the horse back to the start of the race.
	 */
	public void reset() {
		distance = 0;
		handicap = (int)((Math.random() * 10) - 5);
	}

	/**
	 * Gets the distance of the horse along the race track.
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
	
	public double getRaceComplete() {
		
		return (double)distance / 10000;
	}

	public String getName() {
		return name;
	}
	
	
	
}