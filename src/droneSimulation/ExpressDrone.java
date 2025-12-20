package droneSimulation;

public class ExpressDrone extends Drone {

    public ExpressDrone(Position p)
    {
        super("Express" , 45 , 1.0 , p);
    }

    public double calculateConsumption(double d)
    {
        return d * 4;
    }
}