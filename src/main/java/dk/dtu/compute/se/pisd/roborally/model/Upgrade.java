package dk.dtu.compute.se.pisd.roborally.model;

public class Upgrade {
    private String name;
    private int cost;
    private String effect; // Simplified (for now)

    public Upgrade(String name, int cost, String effect) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getEffect() {
        return effect;
    }
}
