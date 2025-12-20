package droneSimulation;

public class StandardPackage implements Deliverable {

    private double weight;
    private Position destination;

    public StandardPackage(double w , Position d)
    {
        weight = w;
        destination = d;
    }

    public double getWeight()
    {
        return weight;
    }

    public Position getDestination()
    {
        return destination;
    }
}
