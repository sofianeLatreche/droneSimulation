package droneSimulation;

public class DeliveryZone {
    private Position center; 
    private double radius; 
    
    public DeliveryZone(Position center, double radius) {
        this.center = center; //for storing the center point of the circle 
        this.radius = radius; //for storing how far the zone extends 
    }
    public boolean contains(Position position) {
        if (position == null || center == null) return false; //just for safety checking 
        return center.distanceTo(position) <= radius;
    }
    public Position getCenter() { return center; }
    public double getRadius() { return radius; }
    public void setCenter(Position center) { this.center = center; }
    public void setRadius(double radius) { this.radius = radius; }
    
    @Override
    public String toString() {
        return String.format("DeliveryZone[center=%s, radius=%.2fkm]", center, radius);
    }
}
