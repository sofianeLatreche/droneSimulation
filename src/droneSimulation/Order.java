package droneSimulation;
public class Order {
    private static int nextId = 1;
    
    public enum Status {
        PENDING,
        IN_PROGRESS,
        DELIVERED,
        FAILED
    }
    public enum Urgency {
        NORMAL,
        EXPRESS
    }
    private int id;
    private String client;
    private Deliverable deliverable;
    private double cost; // in DZD
    private Urgency urgency;
    private Status status;
    private int assignedDroneId;
    
    public Order(String client, Deliverable deliverable, Urgency urgency, double initialCost) {
        this.id = nextId++;
        this.client = client;
        this.deliverable = deliverable;
        this.cost = initialCost; 
        this.urgency = urgency;
        this.status = Status.PENDING;
        this.assignedDroneId = -1; 
    }
    public int getId() { return id; }
    public String getClient() { return client; }
    public Deliverable getDeliverable() { return deliverable; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public Urgency getUrgency() { return urgency; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public int getAssignedDroneId() { return assignedDroneId; }
    public void setAssignedDroneId(int droneId) { this.assignedDroneId = droneId; }
 
    public double getWeight() {
        return deliverable.getWeight();
    }
    
    public Position getDestination() {
        return deliverable.getDestination();
    }
    
    public boolean isExpress() {
        return urgency == Urgency.EXPRESS;
    }
    
    @Override
    public String toString() {
        return String.format("Order#%d for %s | %s | Cost: %.2f DZD | Status: %s | Drone: %s",
                            id, client, deliverable, cost, status,
                            assignedDroneId > 0 ? "#" + assignedDroneId : "Not assigned");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return id == order.id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
}
