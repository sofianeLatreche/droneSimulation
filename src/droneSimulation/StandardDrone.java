package droneSimulation;

public class StandardDrone extends Drone {

    public StandardDrone(Position p)
    {
        super("Standard" , 30 , 1.0 , p);
    }

    public double calculateConsumption(double d)
    {
        return d * 3;
    }
}