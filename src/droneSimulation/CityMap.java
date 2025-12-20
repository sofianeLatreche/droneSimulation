package droneSimulation;
import java.util.List;

public class CityMap {

    private List<DeliveryZone> deliveryZones;
    private List<NoFlyZone> noFlyZones;

    public CityMap(List<DeliveryZone> dz , List<NoFlyZone> nfz)
    {
        deliveryZones = dz;
        noFlyZones = nfz;
    }
    
    public boolean isAllowed(Position p)
    {
        for (NoFlyZone z : noFlyZones)
        {
            if (z.contains(p))
                return false;
        }

        return true;
    }
    public boolean isForbidden(Position p)
    {
        return !isAllowed(p);
    }
}

