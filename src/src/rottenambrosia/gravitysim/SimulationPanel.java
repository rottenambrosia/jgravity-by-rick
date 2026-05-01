package rottenambrosia.gravitysim;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static rottenambrosia.gravitysim.Constants.*;

public class SimulationPanel extends JPanel implements ActionListener {

    List<Body> bodyList = new ArrayList<>();
    public double spawnX, spawnY;
    public double mouseX, mouseY;

    Timer timer;

    public SimulationPanel() {
        bodyList.add(new Body(100, 250, 0.07,  0.02, 4e14, 15, Color.CYAN));
        bodyList.add(new Body(150, 450, 0.03,  0.07, 3e14, 15, Color.YELLOW));
        bodyList.add(new Body(550, 400, 0.03,  -0.05, 2e14, 15, Color.ORANGE));
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        timer = new Timer(16, this);
        timer.start();
    }



     /**
      * <h1>Newton's formula for Gravitation</h1>
     * @param bodies: List of Bodies
     * @description:
     * Computes the mutual gravitational forces between two <code>Body</code> objects and updates their velocities.
     * <p>
     * The method calculates the force using Newton's law of universal gravitation:
     * <pre>F = G * a.mass * b.mass / (distance * SCALE)²</pre>
     * where:
     * <ul>
     *   <li>{@code dx}, {@code dy} are the differences in position between the two bodies,</li>
     *   <li>{@code distance} is the Euclidean distance computed via {@link Math#sqrt(double)},</li>
     *   <li>{@code r_x_unit}, {@code r_y_unit} are the components of the unit vector in the direction from body A to B.</li>
     * </ul>
     * </p>
     * <p>
     * The resulting acceleration is applied to both bodies in opposite directions:
     * <ul>
     *   <li>Body A's velocity is updated toward body B,</li>
     *   <li>Body B's velocity is updated toward body A.</li>
     * </ul>
     * </p>
     */
    public void applyGravity (List<Body> bodies) {
        for (int i = 0; i < bodies.size(); i++){
            for (int j = i+1; j < bodies.size(); j++){
                Body A = bodies.get(i);
                Body B = bodies.get(j);
                double dx = B.x - A.x;
                double dy = B.y - A.y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                double minDist = A.radius + B.radius;
                if (distance < minDist) {
                    distance = minDist;
                }

                double r_x_unit = dx / distance;
                double r_y_unit = dy / distance;

                double F = (G * A.mass * B.mass) / Math.pow(distance, 2);
                // Standard notation suggests : aAB -> acceleration of A due to force applied by B
                double accn_A_B_x = r_x_unit * F / A.mass;
                double accn_A_B_y = r_y_unit * F / A.mass;
                //updating A's velocities due to forces from B
                A.v_x += accn_A_B_x;
                A.v_y += accn_A_B_y;
                //Computing for B
                double accn_B_A_x = (-r_x_unit) * F / B.mass;
                double accn_B_A_y = (-r_y_unit) * F / B.mass;
                //updating B's velocities due to forces from A
                B.v_x += accn_B_A_x;
                B.v_y += accn_B_A_y;
            }
        }
    }

    /**
     * <h1>Collision Checker</h1>
     * @param bodies List of objects of class <code>Body</code>
     * <br>
     * Checks for collision using mathematical inferences
     */
    public void checkCollision (List<Body> bodies) {
        List<Body> toRemove = new ArrayList<Body>();
        List<Body> toAdd = new ArrayList<Body>();
        if (bodies.size()>=2){
            for (int i = 0; i < bodies.size(); i++) {
                for (int j = i + 1; j < bodies.size(); j++) {
                    Body A = bodies.get(i);
                    Body B = bodies.get(j);
                    double distance = Math.sqrt(Math.pow(A.x-B.x, 2) + Math.pow(A.y-B.y, 2));
                    if (distance<=A.radius+B.radius) {
                        System.out.println("Collision hath occured.");
                        double combinedMass = A.mass + B.mass;
                        double v_new_x = (A.mass * A.v_x + B.mass * B.v_x)/combinedMass;
                        double v_new_y = (A.mass * A.v_y + B.mass * B.v_y)/combinedMass;
                        double combinedRadius = Math.cbrt(Math.pow(A.radius, 3) + Math.pow(B.radius, 3));
                        Body combinedBody = new Body(A.x, A.y, v_new_x, v_new_y, combinedMass, combinedRadius, Color.PINK);
                        toAdd.add(combinedBody);
                        toRemove.add(A);
                        toRemove.add(B);
                        System.out.println(combinedBody.v_x+ " " + combinedBody.v_y);

                    }
                }
            }
            bodies.removeAll(toRemove);
            bodyList.addAll(toAdd);
        }
    }
    @Override
    public void actionPerformed (ActionEvent e) { //gets called every 60th of a second
        applyGravity(bodyList);
        bodyList.forEach(Body::update);
        checkCollision(bodyList);
//        if (body1.x + body1.radius >= getWidth() || body1.x + body1.radius <=0 ){
//            body1.v_x*=-1;
//        }
//        if (body1.y + body1.radius >= getHeight() || body1.y + body1.radius <= 0) {
//            body1.v_y*=-1;
//        }
        repaint();
    }

    public void userInteraction () {

    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        for (Body body : bodyList) {
            body.draw(g);
        }
    }
}
