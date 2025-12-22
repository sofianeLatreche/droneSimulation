package droneSimulation;
package com;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   DRONE DELIVERY SIMULATION SYSTEM");
        System.out.println("   City of Boumerdes - Java OOP Project");
        System.out.println("==========================================");
        
        Simulator simulator = new Simulator();
        
        if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            simulator.runQuickTest();
        } else {
            simulator.runSimulation();
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Project by: Sofiane and Halla");
        System.out.println("=".repeat(50));
    }
}
