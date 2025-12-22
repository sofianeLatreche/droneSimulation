package droneSimulation;


public class ExpressDrone extends Drone {
    private static final double CONSUMPTION_PER_KM = 4.0; 
    private static final double BASE_SPEED = 45.0; 
    private static final double BASE_CAPACITY = 1.0; 
    
    public ExpressDrone(Position startPosition) {
        super(startPosition, "ExpressDrone", BASE_SPEED, BASE_CAPACITY);
    }
    
    @Override
    public double calculateConsumption(double distance) {
        return distance * CONSUMPTION_PER_KM;
    }
    
    @Override
    public double getSpeed() {
        if (isBatteryLow()) {
            return 16.0;
        }
        return super.getSpeed();
    }
    
    @Override
    public String toString() {
        return "Express" + super.toString();
    }
}
