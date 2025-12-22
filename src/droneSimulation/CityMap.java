package droneSimulation;
import java.util.ArrayList;
import java.util.List;
public class CityMap {

    private List<DeliveryZone> deliveryZones; //good areas
    private List<NoFlyZone> noFlyZones; //bad areas
    
    public Map() {
        this.deliveryZones = new ArrayList<>();
        this.noFlyZones = new ArrayList<>();
    }
    public boolean isAllowed(Position position) {
        if (position == null) return false; //safety check 
        
        for (NoFlyZone zone : noFlyZones) {
            if (zone.contains(position)) {
                return false;
            }
        }
        return true;
    }
  
    public boolean isForbidden(Position position) {  //just clear things up !
        return !isAllowed(position);
    }
    
    public void addDeliveryZone(DeliveryZone zone) {
        if (zone != null) {
            deliveryZones.add(zone);
        }
    }
    
    public void addNoFlyZone(NoFlyZone zone) {
        if (zone != null) {
            noFlyZones.add(zone);
        }
    }
    public List<DeliveryZone> getDeliveryZones() { return new ArrayList<>(deliveryZones); }
    public List<NoFlyZone> getNoFlyZones() { return new ArrayList<>(noFlyZones); }
    
    @Override
    public String toString() {
        return String.format("Map[DeliveryZones=%d, NoFlyZones=%d]", 
                            deliveryZones.size(), noFlyZones.size());
    }
}

        return true;
    }
    public boolean isForbidden(Position p)
    {
        return !isAllowed(p);
    }
}

