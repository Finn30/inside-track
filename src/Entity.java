abstract class Entity {
    private int id;

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

class Player extends Entity {
    private String name;

    public Player(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Horse extends Entity {
    private String name;
    private String color;

    public Horse(int id, String name, String color) {
        super(id);
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}

// Balance class
class Balance extends Entity {
    private double balanceAmount;

    public Balance(int id, double balanceAmount) {
        super(id);
        this.balanceAmount = balanceAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void addBalance(double amount) {
        this.balanceAmount += amount;
    }
}

class Race extends Entity {
    private String date;

    public Race(int id, String date) {
        super(id);
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
