package droneSimulation;

import java.util.ArrayList;
import java.util.List;
public class ControlCenter {
    private static int totalDeliveries = 0;
    private static double totalDistance = 0.0;
    private static double totalEnergyConsumed = 0.0;
    
    private List<Drone> fleet;
    private List<Order> pendingOrders;
    private List<Order> processedOrders;
    private Position base;
    private Map map;
    
    public ControlCenter(Position base, Map map) {
        this.fleet = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
        this.processedOrders = new ArrayList<>();
        this.base = base;
        this.map = map;
    }
    
    public void addDrone(Drone drone) {
        if (drone != null) {
            fleet.add(drone);
        }
    }
    
    public void addOrder(Order order) {
        if (order != null) {
            pendingOrders.add(order);
        }
    }

    public Drone findDroneForOrder(Order order) {
        if (order == null || order.getDestination() == null) {
            return null;
        }
        
        for (Drone drone : fleet) {
            if (drone.getStatus() != Drone.Status.AVAILABLE) {
                continue;
            }
         
            if (drone.getCapacity() < order.getWeight()) {
                continue;
            }
          
            if (!map.isAllowed(order.getDestination())) {
                continue;
            }
            
            
            double distanceToDestination = drone.getPosition().distanceTo(order.getDestination());
            double distanceBackToBase = order.getDestination().distanceTo(base);
            double roundTripDistance = distanceToDestination + distanceBackToBase;
            
            if (!drone.canFlyTo(order.getDestination())) {
                continue;
            }
            
         
            return drone;
        }
        
        return null; 
    }
    
    
    public double calculateDeliveryCost(Order order, Drone drone) {
        if (order == null || drone == null) {
            return 0.0;
        }
        
        double initialPrice = order.getCost(); // Original product price
        double distanceToDestination = drone.getPosition().distanceTo(order.getDestination());
        double distanceBackToBase = order.getDestination().distanceTo(base);
        double roundTripDistance = distanceToDestination + distanceBackToBase;
        

        double consumption = drone.calculateConsumption(roundTripDistance);
        
        double operationCost = (roundTripDistance * 0.1) + (consumption * 0.02) + 20.0;
        
       
        double insuranceBase = Math.max(initialPrice * 0.02, 10.0);
        double expressSurcharge = order.isExpress() ? 20.0 : 0.0;
        double insurance = insuranceBase + expressSurcharge;
        
        
        double deliveryFee = operationCost + insurance;
        
        return deliveryFee;
    }
    
 
    public boolean assignOrder(Order order) {
        Drone drone = findDroneForOrder(order);
        
        if (drone != null) {
            
            double deliveryCost = calculateDeliveryCost(order, drone);
           
            order.setCost(deliveryCost); // Overwrite with delivery fee
            order.setStatus(Order.Status.IN_PROGRESS);
            order.setAssignedDroneId(drone.getId());
            drone.setStatus(Drone.Status.IN_DELIVERY);
            drone.setCurrentDestination(order.getDestination());
            pendingOrders.remove(order);
            processedOrders.add(order);
            
            return true;
        } else {
            
            return false;
        }
    }
    
    public void completeDelivery(Order order, Drone drone) {
        if (order == null || drone == null) {
            return;
        }
        
        order.setStatus(Order.Status.DELIVERED);
        drone.setStatus(Drone.Status.RETURN_TO_BASE);
        drone.setCurrentDestination(base);
        totalDeliveries++;
        totalDistance += drone.getPosition().distanceTo(base);
        totalEnergyConsumed += drone.calculateConsumption(drone.getPosition().distanceTo(base));
    }

    public void failDelivery(Order order, Drone drone) {
        if (order == null || drone == null) {
            return;
        }
        
        order.setStatus(Order.Status.FAILED);
        drone.setStatus(Drone.Status.RETURN_TO_BASE);
        drone.setCurrentDestination(base);
    }
    
    public List<Drone> getAvailableDrones() {
        List<Drone> available = new ArrayList<>();
        for (Drone drone : fleet) {
            if (drone.getStatus() == Drone.Status.AVAILABLE) {
                available.add(drone);
            }
        }
        return available;
    }

    public List<Drone> getDronesInDelivery() {
        List<Drone> inDelivery = new ArrayList<>();
        for (Drone drone : fleet) {
            if (drone.getStatus() == Drone.Status.IN_DELIVERY) {
                inDelivery.add(drone);
            }
        }
        return inDelivery;
    }
    
    public List<Order> getOrdersByStatus(Order.Status status) {
        List<Order> result = new ArrayList<>();
        for (Order order : processedOrders) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        for (Order order : pendingOrders) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }
    
    public Drone getMostActiveDrone() {
        if (fleet.isEmpty()) return null;
        
        Drone mostActive = fleet.get(0);
        for (Drone drone : fleet) {
            if (drone.getTotalDistance() > mostActive.getTotalDistance()) {
                mostActive = drone;
            }
        }
        return mostActive;
    }
        public List<Drone> getFleet() { return new ArrayList<>(fleet); }
    public List<Order> getPendingOrders() { return new ArrayList<>(pendingOrders); }
    public List<Order> getProcessedOrders() { return new ArrayList<>(processedOrders); }
    public Position getBase() { return base; }
    public Map getMap() { return map; }
    
  
    public static int getTotalDeliveries() { return totalDeliveries; }
    public static double getTotalDistance() { return totalDistance; }
    public static double getTotalEnergyConsumed() { return totalEnergyConsumed; }
    
    @Override
    public String toString() {
        return String.format("ControlCenter[Base: %s | Drones: %d | Pending Orders: %d | Processed: %d]",
                            base, fleet.size(), pendingOrders.size(), processedOrders.size());
    }
}
