package droneSimulation;

public class Position {
    private double x; 
    private double y; 
    
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Position other) {
        if (other == null) return 0;
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
    
    // Move towards destination by a certain step
    public void moveTo(Position destination, double step) {
        if (destination == null || step <= 0) return;
        
        double distance = distanceTo(destination);
        if (distance <= step) {
            this.x = destination.x;
            this.y = destination.y;
        } else {
            double ratio = step / distance;
            this.x = this.x + (destination.x - this.x) * ratio;
            this.y = this.y + (destination.y - this.y) * ratio;
        }
    }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    
    @Override
    public String toString() {
        return String.format("Position(%.2f, %.2f)", x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return Math.abs(position.x - x) < 0.001 && Math.abs(position.y - y) < 0.001; //beacuse floating numbers are not precise 
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);//creates a unique code for each objects so it is easy to find and use 
    }
}
