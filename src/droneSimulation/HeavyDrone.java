package droneSimulation;
public class HeavyDrone extends Drone {
    private static final double CONSUMPTION_PER_KM = 5.0; 
    private static final double BASE_SPEED = 20.0; 
    private static final double BASE_CAPACITY = 3.0; 
    
    public HeavyDrone(Position startPosition) {
        super(startPosition, "HeavyDrone", BASE_SPEED, BASE_CAPACITY);
    }
    
    @Override
    public double calculateConsumption(double distance) {
        return distance * CONSUMPTION_PER_KM;
    }
    
    @Override
    public String toString() {
        return "Heavy" + super.toString();
    }
}
