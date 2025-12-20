package droneSimulation;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Position other) {
        return Math.hypot(this.x - other.x, this.y - other.y);
    }

    public void moveTo(Position destination, double step) {
        double distance = distanceTo(destination);
        if (distance <= step) {
            this.x = destination.x;
            this.y = destination.y;
            return;
        }
        this.x += (destination.x - x) / distance * step;
        this.y += (destination.y - y) / distance * step;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    @Override
    public boolean equals(Object o)
    {
        Position p = (Position) o;

        if (x == p.x && y == p.y)
            return true;

        return false;
    }

    @Override
    public String toString()
    {
        return "x = " + x + " , y = " + y;
    }
}


