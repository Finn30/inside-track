class Bet extends Entity {
    private int playerId;
    private double betAmount;
    private double totalPayout;
    private boolean isWinner;

    public Bet(int id, int playerId, double betAmount, double totalPayout, boolean isWinner) {
        super(id);
        this.playerId = playerId;
        this.betAmount = betAmount;
        this.totalPayout = totalPayout;
        this.isWinner = isWinner;
    }

    public int getPlayerId() {
        return playerId;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public double getTotalPayout() {
        return totalPayout;
    }

    public boolean isWinner() {
        return isWinner;
    }
}

// History class
class History extends Entity {
    private int playerId;
    private int raceId;
    private int betId;

    public History(int id, int playerId, int raceId, int betId) {
        super(id);
        this.playerId = playerId;
        this.raceId = raceId;
        this.betId = betId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getBetId() {
        return betId;
    }
}
