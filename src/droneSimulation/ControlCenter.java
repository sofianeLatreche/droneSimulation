package droneSimulation;

import java.util.ArrayList;
import java.util.List;

public class ControlCenter {

    private List<Drone> fleet;
    private List<Order> pendingOrders;
    private List<Order> processedOrders;

    private Position base;
    private CityMap map;

    public static int totalDeliveries = 0;

    public ControlCenter(Position b , CityMap m)
    {
        base = b;
        map = m;

        fleet = new ArrayList<>();
        pendingOrders = new ArrayList<>();
        processedOrders = new ArrayList<>();
    }

    public void addDrone(Drone d)
    {
        fleet.add(d);
    }

    public Drone findDroneForOrder(Order o)
    {
        for (Drone d : fleet)
        {
            if (d.getCapacity() >= o.getDeliverable().getWeight()
                    && d.canFlyTo(o.getDeliverable().getDestination())
                    && map.isAllowed(o.getDeliverable().getDestination())
                    && d.getStatus().equals("AVAILABLE"))
            {
                return d;
            }
        }

        return null;
    }

    public boolean assignOrder(Order o)
    {
        Drone d = findDroneForOrder(o);

        if (d == null)
        {
            pendingOrders.add(o);
            return false;
        }

        double price = calculateDeliveryCost(o , d);

        o.setCost(price);
        o.setStatus("IN PROGRESS");

        d.setStatus("IN DELIVERY");

        processedOrders.add(o);
        totalDeliveries++;

        return true;
    }

    public double calculateDeliveryCost(Order o , Drone d)
    {
        double dist = base.distanceTo(o.getDeliverable().getDestination());
        dist = dist * 2;

        double cons = d.calculateConsumption(dist);

        double op = dist * 0.1 + cons * 0.02 + 20;

        double ins = o.getCost() * 0.02;

        if (ins < 10)
            ins = 10;

        if (o.getUrgency().equals("EXPRESS"))
            ins = ins + 20;

        return op + ins;
    }
}

