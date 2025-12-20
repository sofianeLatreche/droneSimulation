package droneSimulation;

import javax.swing.*;
import java.awt.*;

public class Main extends JPanel implements Runnable {

    private double x = 50, y = 50;
    private int radius = 30;
    private double dx = 0, dy = 0;
    private final double gravity = 0.1;
    private final double bouncing = 0.2;
    private final double stopThreshold = 2; 

    public Main() {
        setBackground(Color.WHITE);
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GRAY);
        g.fillRect((int)x, (int)y, 40, 15);

        g.setColor(Color.BLACK);
        g.fillRect((int)x + 3, (int)y - 5, 15, 3);  
        g.fillRect((int)x + 22, (int)y - 5, 15, 3); 

        g.setColor(Color.DARK_GRAY);
        g.drawLine((int)x + 5, (int)y + 15, (int)x - 5, (int)y + 25);   
        g.drawLine((int)x + 35, (int)y + 15, (int)x + 45, (int)y + 25); 
    }


    @Override
    public void run() {
        while (true) {
            x += dx;
            y += dy;

            if (x < 0) {
                x = 0;
                dx = -dx * bouncing;
            } else if (x > getWidth() - radius) {
                x = getWidth() - radius;
                dx = -dx * bouncing;
            }

            if (y >= getHeight() - radius) {
                y = getHeight() - radius;

                if (Math.abs(dy) < stopThreshold) {
                    dy = 0;  
                    dx = 0;  
                } else {
                    dy = -dy * bouncing;
                }
            } else {
                dy += gravity;
            }

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bouncing Ball");
        Main panel = new Main();

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(panel).start();
    }
}