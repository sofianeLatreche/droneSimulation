package droneSimulation;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("==========================================");
            System.out.println("   DRONE DELIVERY SIMULATION SYSTEM");
            System.out.println("   City of Boumerdes - Java OOP Project");
            System.out.println("==========================================");

            Simulator simulator = new Simulator();

            if (simulator == null) {
                System.err.println("Simulator initialization failed.");
                return;
            }

            try {
                if (args != null && args.length > 0 && args[0].equalsIgnoreCase("test")) {
                    simulator.runQuickTest();
                } else {
                    simulator.runSimulation();
                }
            } catch (Exception e) {
                System.err.println("Simulation error: " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(50));
            System.out.println("Project by: Sofiane and Halla");
            System.out.println("=".repeat(50));

        } catch (Exception e) {
            System.err.println("Fatal error in main: " + e.getMessage());
        }
    }
}
