package droneSimulation;

public class Order {

    private static int count = 0;

    private int id;
    private String client;
    private Deliverable deliverable;
    private double cost;
    private String urgency;
    private String status;

    public Order(String c , Deliverable d , String u , double price)
    {
        id = ++count;
        client = c;
        deliverable = d;
        urgency = u;
        cost = price;
    }

    public Deliverable getDeliverable()
    {
        return deliverable;
    }

    public String getUrgency()
    {
        return urgency;
    }

    public double getCost()
    {
        return cost;
    }

    public void setCost(double c)
    {
        cost = c;
    }

    public void setStatus(String s)
    {
        status = s;
    }

    public boolean equals(Object o)
    {
        Order ord = (Order) o;

        return id == ord.id;
    }
}