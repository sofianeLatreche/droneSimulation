package droneSimulation;

public class StandardPackage implements Deliverable {
    private static int nextId = 1;
    
    private int id;
    private String description;
    private double weight; 
    private Position destination;
    
    public StandardPackage(String description, double weight, Position destination) {
        this.id = nextId++;
        this.description = description;
        this.weight = weight;
        this.destination = destination;
    }
    @Override
    public double getWeight() {
        return weight;
    } 
    @Override
    public Position getDestination() {
        return destination;
    }
    public int getId() { return id; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        return String.format("Package#%d [%s] %.2fkg to %s", 
                            id, description, weight, destination);
    }
}
