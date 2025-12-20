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
}