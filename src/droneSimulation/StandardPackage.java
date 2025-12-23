package droneSimulation;
package com;

public class StandardPackage implements Deliverable {
    private static int nextId = 1;
    
    private int id;
    private String description;
    private double weight; 
    private Position destination;
    
    public StandardPackage(String description, double weight, Position destination) {
        this.id = nextId++;
        this.description = description;
        this.weight = weight;
        this.destination = destination;
    }
}
