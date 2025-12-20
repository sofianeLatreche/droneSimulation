package droneSimulation;

public class HeavyDrone extends Drone {

    public HeavyDrone(Position p)
    {
        super("Heavy" , 20 , 3.0 , p);
    }

    public double calculateConsumption(double d)
    {
        return d * 5;
    }

    @Override
    public void flyTo(Position dest , double step)
    {
        if (battery < 20)
            speed = 16;

        super.flyTo(dest , step);
    }
}