package droneSimulation;

public class DeliveryZone {

    protected Position center;
    protected double radius;

    public DeliveryZone(Position c , double r)
    {
        center = c;
        radius = r;
    }
    
    public boolean contains(Position p)
    {
        if (center.distanceTo(p) <= radius)
            return true;

        return false;
    }
}