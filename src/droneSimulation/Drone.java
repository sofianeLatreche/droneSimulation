package droneSimulation;
import java.util.ArrayList;
import java.util.List;

public abstract class Drone {
    // Static counter for auto-incrementing IDs
    private static int nextId = 1;
    
    public enum Status {
        AVAILABLE,
        IN_DELIVERY,
        RETURN_TO_BASE
    }
    protected int id;
    protected Position position;
    protected double battery; // 0-100%
    protected String model;
    protected double speed; // km/h
    protected double capacity; // kg
    protected Status status;
    protected double totalDistance;
    protected List<Position> positionHistory;
    protected Position currentDestination;
    
    public Drone(Position startPosition, String model, double speed, double capacity) {
        this.id = nextId++;
        this.position = startPosition;
        this.battery = 100.0;
        this.model = model;
        this.speed = speed;
        this.capacity = capacity;
        this.status = Status.AVAILABLE;
        this.totalDistance = 0.0;
        this.positionHistory = new ArrayList<>();
        this.positionHistory.add(startPosition);
        this.currentDestination = null;
    }
    
    public abstract double calculateConsumption(double distance);
    
    // Check if drone can fly to destination (enough battery for round trip)
    public boolean canFlyTo(Position destination) {
        if (destination == null) return false;
        
        double distanceToDestination = position.distanceTo(destination);
        double roundTripDistance = distanceToDestination * 2;
        double consumption = calculateConsumption(roundTripDistance);
        
        return battery >= consumption;
    }
    
    public boolean flyTo(Position destination) {
        if (destination == null || !canFlyTo(destination)) {
            return false;
        }
        
        double distance = position.distanceTo(destination);
        double consumption = calculateConsumption(distance);

        position = destination;
        totalDistance += distance;
        battery -= consumption;
        
        positionHistory.add(destination);
        
        if (status == Status.IN_DELIVERY && currentDestination != null && 
            currentDestination.equals(destination)) {
            // Reached delivery destination
            currentDestination = null;
        }
        
        return true;
    }
    public boolean moveStep(Position destination) {
        if (destination == null || position.equals(destination)) {
            return true; // Already at destination
        }
        
        double distancePerMinute = speed / 60.0;
        position.moveTo(destination, distancePerMinute);
        Position oldPosition = positionHistory.get(positionHistory.size() - 1);
        double distanceMoved = oldPosition.distanceTo(position);
        double consumption = calculateConsumption(distanceMoved);
        battery -= consumption;
        totalDistance += distanceMoved;
           if (!position.equals(oldPosition)) {
            positionHistory.add(new Position(position.getX(), position.getY()));
        }
        
        if (position.distanceTo(destination) < 0.01) { // 10 meter tolerance
            position = destination;
            return true;
        }
        
        return false;
    }
    
    public void recharge(double percentage) {
        if (percentage > 0) {
            battery = Math.min(100.0, battery + percentage);
        }
    }
    public boolean isBatteryLow() {
        return battery < 20.0;
    }
    public int getId() { return id; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public double getBattery() { return battery; }
    public String getModel() { return model; }
    public double getSpeed() { return speed; }
    public double getCapacity() { return capacity; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public double getTotalDistance() { return totalDistance; }
    public List<Position> getPositionHistory() { return new ArrayList<>(positionHistory); }
    public Position getCurrentDestination() { return currentDestination; }
    public void setCurrentDestination(Position dest) { this.currentDestination = dest; }
    
    @Override
    public String toString() {
        return String.format("Drone#%d [%s] @%s | Battery: %.1f%% | Status: %s | Distance: %.2fkm",
                            id, model, position, battery, status, totalDistance);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Drone drone = (Drone) obj;
        return id == drone.id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
}
