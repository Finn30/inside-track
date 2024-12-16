public abstract class AbstractHorse {
    protected int distance;
    protected int handicap;
    protected String name;
    protected String imageFolder;

    public AbstractHorse(String name, String imageFolder) {
        this.name = name;
        this.imageFolder = imageFolder;
    }

    public abstract void run();

    public void reset() {
        distance = 0;
        handicap = (int) ((Math.random() * 10) - 5);
    }

    public int getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getImageFolder() {
        return imageFolder;
    }
}