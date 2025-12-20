package droneSimulation;

import java.util.List;

public class Simulator {

    public static void run(ControlCenter cc , List<Order> orders)
    {
        for (int minute = 0 ; minute < 480 ; minute++)
        {
            for (Order o : orders)
            {
                cc.assignOrder(o);
            }
        }
    }
}