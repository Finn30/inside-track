public class Player {
    private String name;
    private int points;

    public Player(String name, int initialPoints) {
        this.name = name;
        this.points = PlayerDB.getPlayerPoints(name);
        if (this.points == 0) { // Jika poin 0, asumsi pemain baru
            PlayerDB.addPlayer(name, initialPoints); // Tambahkan ke database
            this.points = initialPoints;
        }
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
        PlayerDB.updatePlayerPoints(name, this.points);
    }

    public void subtractPoints(int points) {
        this.points -= points;
        PlayerDB.updatePlayerPoints(name, this.points);
    }
}