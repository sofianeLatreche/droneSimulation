package droneSimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    private ControlCenter controlCenter;
    private Random random;
    private int simulationTime; // in minutes
    private List<Order> allOrders; // Track all orders generated
    
  
    private static final int SIMULATION_DURATION = 480; // 8 hours in minutes
    private static final double ORDER_GENERATION_PROBABILITY = 0.3; // 30% chance per minute
    private static final Position CITY_CENTER = new Position(0, 0);
    private static final double CITY_RADIUS = 10.0; // km
    
    public Simulator() {
       
        Map map = createCityMap();
        
       
        this.controlCenter = new ControlCenter(CITY_CENTER, map);
        this.random = new Random();
        this.simulationTime = 0;
        this.allOrders = new ArrayList<>();
        

        initializeDrones();
    }
    
    private Map createCityMap() {
        Map map = new Map();
        
     
        map.addDeliveryZone(new DeliveryZone(new Position(0, 0), 8.0));
        map.addDeliveryZone(new DeliveryZone(new Position(5, 5), 3.0));
        map.addDeliveryZone(new DeliveryZone(new Position(-5, -5), 3.0));
        map.addNoFlyZone(new NoFlyZone(new Position(7, 7), 1.5));
        map.addNoFlyZone(new NoFlyZone(new Position(-7, -7), 1.5));
        map.addNoFlyZone(new NoFlyZone(new Position(0, 8), 1.0));
        
        return map;
    }
    
    private void initializeDrones() {
     
        controlCenter.addDrone(new StandardDrone(CITY_CENTER));
        controlCenter.addDrone(new StandardDrone(CITY_CENTER));
        controlCenter.addDrone(new ExpressDrone(CITY_CENTER));
        controlCenter.addDrone(new HeavyDrone(CITY_CENTER));
        controlCenter.addDrone(new HeavyDrone(CITY_CENTER));
    }
    
    private Order generateRandomOrder(int minute) {
        String[] clients = {"Ahmed", "Fatima", "Mohamed", "Yasmine", "Karim", "Leila", "Omar", "Zahra"};
        String client = clients[random.nextInt(clients.length)] + " " + (minute % 1000);
        String[] descriptions = {"Food", "Medicine", "Electronics", "Clothes", "Books", "Documents"};
        String description = descriptions[random.nextInt(descriptions.length)];
        
        double weight = 0.1 + random.nextDouble() * 2.4;
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius = random.nextDouble() * 7.0; // Within city radius
        double x = CITY_CENTER.getX() + radius * Math.cos(angle);
        double y = CITY_CENTER.getY() + radius * Math.sin(angle);
        Position destination = new Position(x, y);
        
        Order.Urgency urgency = random.nextDouble() < 0.2 ? Order.Urgency.EXPRESS : Order.Urgency.NORMAL;
        
        double initialPrice = 50 + random.nextDouble() * 450;
        
        StandardPackage pack = new StandardPackage(description, weight, destination);
        return new Order(client, pack, urgency, initialPrice);
    }
    
    public void runSimulation() {
        System.out.println(" Starting Drone Delivery Simulation");
        System.out.println(" Duration: 8 hours (480 minutes)");
        System.out.println(" Initial Fleet: " + controlCenter.getFleet().size() + " drones");
        System.out.println("=".repeat(60));
        
        for (int minute = 0; minute < SIMULATION_DURATION; minute++) {
            simulationTime = minute;
            
            if (random.nextDouble() < ORDER_GENERATION_PROBABILITY) {
                Order newOrder = generateRandomOrder(minute);
                controlCenter.addOrder(newOrder);
                allOrders.add(newOrder);
                
                if (minute % 60 == 0) { // Log every hour
                    System.out.printf("[Minute %03d] New order: %s\n", minute, newOrder);
                }
            }
            
            List<Order> pending = new ArrayList<>(controlCenter.getPendingOrders());
            for (Order order : pending) {
                if (controlCenter.assignOrder(order)) {
                    if (minute % 60 == 0) {
                        System.out.printf("[Minute %03d] Order #%d assigned\n", minute, order.getId());
                    }
                }
            }
            
            for (Drone drone : controlCenter.getDronesInDelivery()) {
                Position destination = drone.getCurrentDestination();
                
                if (destination != null) {
                    boolean reached = drone.moveStep(destination);
                    
                    if (reached) {
                        for (Order order : controlCenter.getProcessedOrders()) {
                            if (order.getAssignedDroneId() == drone.getId() && 
                                order.getStatus() == Order.Status.IN_PROGRESS) {
                                
                                if (destination.equals(order.getDestination())) {
                                    // Reached delivery destination
                                    controlCenter.completeDelivery(order, drone);
                                    System.out.printf("[Minute %03d]  Delivery completed: Order #%d by Drone #%d\n", 
                                                     minute, order.getId(), drone.getId());
                                } else if (destination.equals(controlCenter.getBase())) {
                                    // Returned to base
                                    drone.setStatus(Drone.Status.AVAILABLE);
                                    drone.setCurrentDestination(null);
                                }
                                break;
                            }
                        }
                    }
                }
            }
            
            for (Drone drone : controlCenter.getFleet()) {
                if (drone.getPosition().distanceTo(controlCenter.getBase()) < 0.1 && 
                    drone.getBattery() < 50.0) {
                    drone.recharge(10.0); // Recharge 10% per minute at base
                }
            }
            
            if (minute % 60 == 0) {
                System.out.printf("[Hour %d] Active: %d drones | Pending: %d orders | Delivered: %d\n",
                                minute / 60 + 1,
                                controlCenter.getDronesInDelivery().size(),
                                controlCenter.getPendingOrders().size(),
                                controlCenter.getOrdersByStatus(Order.Status.DELIVERED).size());
            }
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" SIMULATION COMPLETE");
        printStatistics();
    }
    
    private void printStatistics() {
        System.out.println("\n FINAL STATISTICS");
        System.out.println("-".repeat(40));
        
        int totalOrders = allOrders.size();
        int delivered = controlCenter.getOrdersByStatus(Order.Status.DELIVERED).size();
        int failed = controlCenter.getOrdersByStatus(Order.Status.FAILED).size();
        int pending = controlCenter.getPendingOrders().size();
        
        System.out.printf("Total Orders Generated: %d\n", totalOrders);
        System.out.printf(" Successfully Delivered: %d (%.1f%%)\n", 
                         delivered, totalOrders > 0 ? (delivered * 100.0 / totalOrders) : 0);
        System.out.printf(" Failed Deliveries: %d (%.1f%%)\n", 
                         failed, totalOrders > 0 ? (failed * 100.0 / totalOrders) : 0);
        System.out.printf(" Still Pending: %d\n", pending);
        
        System.out.println("\n DRONE PERFORMANCE:");
        System.out.println("-".repeat(40));
        
        Drone mostActive = controlCenter.getMostActiveDrone();
        if (mostActive != null) {
            System.out.printf("Most Active Drone: %s\n", mostActive);
        }
        
        for (Drone drone : controlCenter.getFleet()) {
            System.out.printf("Drone #%d (%s): %.2f km traveled | Battery: %.1f%%\n",
                            drone.getId(), drone.getModel(),
                            drone.getTotalDistance(), drone.getBattery());
        }
                System.out.println("\n GLOBAL STATISTICS:");
        System.out.println("-".repeat(40));
        System.out.printf("Total Deliveries (global): %d\n", ControlCenter.getTotalDeliveries());
        System.out.printf("Total Distance Traveled: %.2f km\n", ControlCenter.getTotalDistance());
        System.out.printf("Total Energy Consumed: %.2f%% battery\n", ControlCenter.getTotalEnergyConsumed());
        
        double totalRevenue = 0;
        for (Order order : controlCenter.getProcessedOrders()) {
            if (order.getStatus() == Order.Status.DELIVERED) {
                totalRevenue += order.getCost();
            }
        }
        System.out.printf(" Total Revenue: %.2f DZD\n", totalRevenue);
        
        System.out.println("\n  ZONE COMPLIANCE:");
        System.out.println("-".repeat(40));
        int violations = 0;
        for (Drone drone : controlCenter.getFleet()) {
            for (Position pos : drone.getPositionHistory()) {
                if (controlCenter.getMap().isForbidden(pos)) {
                    violations++;
                    break;
                }
            }
        }
        System.out.printf("No-fly zone violations: %d\n", violations);
    }
    
    public void runQuickTest() {
        System.out.println("Running quick test scenario...\n");
        
        Position testDestination = new Position(3, 4);
        StandardPackage testPackage = new StandardPackage("Test Package", 0.5, testDestination);
        Order testOrder = new Order("Test Client", testPackage, Order.Urgency.NORMAL, 100.0);
        
        controlCenter.addOrder(testOrder);
        System.out.println("Test Order Created: " + testOrder);
        
        boolean assigned = controlCenter.assignOrder(testOrder);
        System.out.println("Order Assigned: " + assigned);
        
        if (assigned) {
            Drone assignedDrone = null;
            for (Drone drone : controlCenter.getFleet()) {
                if (drone.getId() == testOrder.getAssignedDroneId()) {
                    assignedDrone = drone;
                    break;
                }
            }
            
            if (assignedDrone != null) {
                System.out.println("Assigned Drone: " + assignedDrone);
                System.out.printf("Delivery Cost: %.2f DZD\n", 
                                controlCenter.calculateDeliveryCost(testOrder, assignedDrone));
            }
        }
        
        System.out.println("\nAvailable Drones: " + controlCenter.getAvailableDrones().size());
        System.out.println("Fleet Status:");
        for (Drone drone : controlCenter.getFleet()) {
            System.out.println("  " + drone);
        }
    }
    
    public ControlCenter getControlCenter() { return controlCenter; }
    public int getSimulationTime() { return simulationTime; }
    public List<Order> getAllOrders() { return new ArrayList<>(allOrders); }
}
