package droneSimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    private ControlCenter controlCenter;
    private Random random;
    private int simulationTime;
    private List<Order> allOrders;

    private static final int SIMULATION_DURATION = 480;
    private static final double ORDER_GENERATION_PROBABILITY = 0.3;
    private static final Position CITY_CENTER = new Position(0, 0);
    private static final double CITY_RADIUS = 10.0;

    public Simulator() {
        try {
            Map map = createCityMap();
            this.controlCenter = new ControlCenter(CITY_CENTER, map);
            this.random = new Random();
            this.simulationTime = 0;
            this.allOrders = new ArrayList<>();
            initializeDrones();
        } catch (Exception e) {
            System.err.println("Failed to initialize simulator: " + e.getMessage());
        }
    }

    private Map createCityMap() {
        Map map = new Map();
        try {
            map.addDeliveryZone(new DeliveryZone(new Position(0, 0), 8.0));
            map.addDeliveryZone(new DeliveryZone(new Position(5, 5), 3.0));
            map.addDeliveryZone(new DeliveryZone(new Position(-5, -5), 3.0));
            map.addNoFlyZone(new NoFlyZone(new Position(7, 7), 1.5));
            map.addNoFlyZone(new NoFlyZone(new Position(-7, -7), 1.5));
            map.addNoFlyZone(new NoFlyZone(new Position(0, 8), 1.0));
        } catch (Exception e) {
            System.err.println("Error creating city map: " + e.getMessage());
        }
        return map;
    }

    private void initializeDrones() {
        try {
            controlCenter.addDrone(new StandardDrone(CITY_CENTER));
            controlCenter.addDrone(new StandardDrone(CITY_CENTER));
            controlCenter.addDrone(new ExpressDrone(CITY_CENTER));
            controlCenter.addDrone(new HeavyDrone(CITY_CENTER));
            controlCenter.addDrone(new HeavyDrone(CITY_CENTER));
        } catch (Exception e) {
            System.err.println("Error initializing drones: " + e.getMessage());
        }
    }

    private Order generateRandomOrder(int minute) {
        try {
            String[] clients = {"Ahmed", "Fatima", "Mohamed", "Yasmine", "Karim", "Leila", "Omar", "Zahra"};
            String client = clients[random.nextInt(clients.length)] + " " + (minute % 1000);

            String[] descriptions = {"Food", "Medicine", "Electronics", "Clothes", "Books", "Documents"};
            String description = descriptions[random.nextInt(descriptions.length)];

            double weight = 0.1 + random.nextDouble() * 2.4;
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = random.nextDouble() * 7.0;
            double x = CITY_CENTER.getX() + radius * Math.cos(angle);
            double y = CITY_CENTER.getY() + radius * Math.sin(angle);

            Position destination = new Position(x, y);
            Order.Urgency urgency = random.nextDouble() < 0.2 ? Order.Urgency.EXPRESS : Order.Urgency.NORMAL;
            double initialPrice = 50 + random.nextDouble() * 450;

            StandardPackage pack = new StandardPackage(description, weight, destination);
            return new Order(client, pack, urgency, initialPrice);
        } catch (Exception e) {
            System.err.println("Error generating order: " + e.getMessage());
            return null;
        }
    }

    public void runSimulation() {
        try {
            for (int minute = 0; minute < SIMULATION_DURATION; minute++) {
                simulationTime = minute;

                if (random.nextDouble() < ORDER_GENERATION_PROBABILITY) {
                    Order newOrder = generateRandomOrder(minute);
                    if (newOrder != null) {
                        controlCenter.addOrder(newOrder);
                        allOrders.add(newOrder);
                    }
                }

                List<Order> pending = new ArrayList<>(controlCenter.getPendingOrders());
                for (Order order : pending) {
                    try {
                        controlCenter.assignOrder(order);
                    } catch (Exception e) {
                        System.err.println("Order assignment failed: " + e.getMessage());
                    }
                }

                for (Drone drone : controlCenter.getDronesInDelivery()) {
                    try {
                        Position destination = drone.getCurrentDestination();
                        if (destination != null && drone.moveStep(destination)) {
                            for (Order order : controlCenter.getProcessedOrders()) {
                                if (order.getAssignedDroneId() == drone.getId() &&
                                    order.getStatus() == Order.Status.IN_PROGRESS &&
                                    destination.equals(order.getDestination())) {

                                    controlCenter.completeDelivery(order, drone);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Drone movement error: " + e.getMessage());
                    }
                }

                for (Drone drone : controlCenter.getFleet()) {
                    try {
                        if (drone.getPosition().distanceTo(controlCenter.getBase()) < 0.1 &&
                            drone.getBattery() < 50.0) {
                            drone.recharge(10.0);
                        }
                    } catch (Exception e) {
                        System.err.println("Recharge error: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Simulation crashed: " + e.getMessage());
        }

        printStatistics();
    }

    private void printStatistics() {
        try {
            int totalOrders = allOrders.size();
            int delivered = controlCenter.getOrdersByStatus(Order.Status.DELIVERED).size();
            int failed = controlCenter.getOrdersByStatus(Order.Status.FAILED).size();

            System.out.println("Total Orders: " + totalOrders);
            System.out.println("Delivered: " + delivered);
            System.out.println("Failed: " + failed);
        } catch (Exception e) {
            System.err.println("Statistics error: " + e.getMessage());
        }
    }

    public ControlCenter getControlCenter() { return controlCenter; }
    public int getSimulationTime() { return simulationTime; }
    public List<Order> getAllOrders() { return new ArrayList<>(allOrders); }
}
