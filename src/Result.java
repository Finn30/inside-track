class Result extends Entity {
    private int horseId;
    private int raceId;
    private int position;

    public Result(int id, int horseId, int raceId, int position) {
        super(id);
        this.horseId = horseId;
        this.raceId = raceId;
        this.position = position;
    }

    public int getHorseId() {
        return horseId;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getPosition() {
        return position;
    }
}