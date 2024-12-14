public class Player {
    private String name;
    private int points;

    public Player(String name, int initialPoints) {
        this.name = name;
        this.points = PlayerDB.getPlayerPoints(name);
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
        if (this.points - points < 0) {
            this.points = 0; // Jika hasil pengurangan kurang dari 0, set poin ke 0
        } else {
            this.points -= points;
        }
        PlayerDB.updatePlayerPoints(this.name, this.points);
    }

}