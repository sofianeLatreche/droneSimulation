package droneSimulation;
public class NoFlyZone {
    private Position center;
    private double radius; 
    
    public NoFlyZone(Position center, double radius) {
        this.center = center;
        this.radius = radius;
    }
    
    public boolean contains(Position position) {
        if (position == null || center == null) return false;
        return center.distanceTo(position) <= radius;
    } 
    public Position getCenter() { return center; }
    public double getRadius() { return radius; }
    public void setCenter(Position center) { this.center = center; }
    public void setRadius(double radius) { this.radius = radius; }
    
    @Override
    public String toString() {
        return String.format("NoFlyZone[center=%s, radius=%.2fkm]", center, radius);
    }
}
