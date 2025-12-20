package droneSimulation;

import java.util.ArrayList;
import java.util.List;

public abstract class Drone {

    private static int count = 0;

    protected int id;
    protected Position position;
    protected double battery;
    protected String model;
    protected double speed;
    protected double capacity;
    protected String status;
    protected double totalDistance;
    protected List<Position> history;

    public Drone(String m , double s , double c , Position p)
    {
        id = ++count;
        model = m;
        speed = s;
        capacity = c;
        position = p;

        battery = 100;
        status = "AVAILABLE";
        totalDistance = 0;

        history = new ArrayList<>();
        history.add(p);
    }
    
    public abstract double calculateConsumption(double d);

    public boolean canFlyTo(Position dest)
    {
        double d = position.distanceTo(dest);
        d = d * 2;

        if (battery >= calculateConsumption(d))
            return true;

        return false;
    }

    public void flyTo(Position dest , double step)
    {
        double before = position.distanceTo(dest);

        position.moveTo(dest , step);

        double after = position.distanceTo(dest);

        double moved = before - after;

        battery = battery - calculateConsumption(moved);
        totalDistance = totalDistance + moved;

        history.add(new Position(position.getX() , position.getY()));
    }

    public void recharge(double p)
    {
        battery = battery + p;

        if (battery > 100)
            battery = 100;
    }

    public double getCapacity()
    {
        return capacity;
    }

    public Position getPosition()
    {
        return position;
    }

    public double getBattery()
    {
        return battery;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String s)
    {
        status = s;
    }

    public double getTotalDistance()
    {
        return totalDistance;
    }

    @Override
    public boolean equals(Object o)
    {
        Drone d = (Drone) o;

        if (id == d.id)
            return true;

        return false;
    }

    @Override
    public String toString()
    {
        String s = "";

        s += "Drone id : " + id + "\n";
        s += "Model : " + model + "\n";
        s += "Battery : " + battery + "\n";
        s += "Status : " + status + "\n";

        return s;
    }
}
