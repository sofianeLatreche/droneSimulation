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
}