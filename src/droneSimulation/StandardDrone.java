package droneSimulation;

public class StandardDrone extends Drone {
    private static final double CONSUMPTION_PER_KM = 3.0; 
    private static final double BASE_SPEED = 30.0;
    private static final double BASE_CAPACITY = 1.0; 
    
    public StandardDrone(Position startPosition) {
        super(startPosition, "StandardDrone", BASE_SPEED, BASE_CAPACITY);
    }
    
    @Override
    public double calculateConsumption(double distance) {
        return distance * CONSUMPTION_PER_KM;
    }
    
    @Override
    public String toString() {
        return "Standard" + super.toString();
    }
}
