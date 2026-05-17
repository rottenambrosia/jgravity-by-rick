/**
 * They tell you, "Read this, eat this, don't look around
 * Just peep this, preach this, teach us, Jesus"
 * Okay, look up now, they done stole yo' streetness
 * After all of that, you received this
 */

package rottenambrosia.gravitysim;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import static rottenambrosia.gravitysim.Constants.*;

public class SimulationPanel extends JPanel implements ActionListener {
    static List<Body> bodyList = new ArrayList<>();
    SpacetimeGrid spacetimeGrid = new SpacetimeGrid();
    MouseInteraction mouseInteraction = new MouseInteraction(bodyList);
    private long frameCount = 0;
    private boolean paused = false;
    private boolean showHelp = false;
    Timer timer;


    public SimulationPanel() {
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE -> paused = !paused;
                    case KeyEvent.VK_C -> bodyList.clear();
                    case KeyEvent.VK_R -> resetScene();
                    case KeyEvent.VK_H -> showHelp = !showHelp;
                    case KeyEvent.VK_ESCAPE ->  System.exit(0);
                }

            }
        });
        setPreferredSize(new Dimension(1920, 1080));
        setBackground(Color.BLACK);
        timer = new Timer(16, this);
        timer.start();
        addMouseListener(mouseInteraction);
        addMouseMotionListener(mouseInteraction);
        resetScene();
    }

    /**
     * Click "r" and reset the scene.
     */
    private void resetScene() {
        bodyList.clear();
        frameCount = 0;
        double starMass = 2e13;
        double sx = 900, sy = 400;
        bodyList.add(new Body(sx, sy, 0, 0, starMass, 25, Color.ORANGE));
//        System.out.println("Orbital speed at r=100: " + Math.sqrt(G * starMass / 100));

        addPlanet(sx, sy, starMass,  55,   0,   1e9,  3,  new Color(169, 169, 169)); // Mercury
        addPlanet(sx, sy, starMass,  85,  40,   2e9,  5,  new Color(218, 138, 3, 221)); // Venus
        addPlanet(sx, sy, starMass, 115,  90,   2e9,  5,  new Color(27, 177, 228)); // Earth
        addPlanet(sx, sy, starMass, 150, 150,   1e9,  4,  new Color(119, 1, 1)); // Mars
        addPlanet(sx, sy, starMass, 200, 220,   8e9,  9,  new Color(201, 144,  57)); // Jupiter
        addPlanet(sx, sy, starMass, 250, 300,   6e9,  8,  new Color(210, 180, 122)); // Saturn
        addPlanet(sx, sy, starMass, 290, 10,    3e9,  6,  new Color(173, 216, 230)); // Uranus
        addPlanet(sx, sy, starMass, 330, 200,   3e9,  6,  new Color( 63,  84, 186)); // Neptune
    }
    public void addPlanet (double sx, double sy, double starMass, double r, double angleDeg, double mass,
                           double radius, Color color) {
        double angle = Math.toRadians(angleDeg);
        double px = sx + r*Math.cos(angle);
        double py = sy + r*Math.sin(angle);
        double speed = Math.sqrt(G*starMass/r);
        double vx = -Math.sin(angle)*speed;
        double vy = Math.cos(angle)*speed;
        bodyList.add(new Body(px, py, vx, vy, mass, radius, color));
    }

    /**
     * <h1>Newton's formula for Gravitation</h1>
     *
     * @param bodies: List of Bodies
     * @description: Computes the mutual gravitational forces between two <code>Body</code> objects and updates their velocities.
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
    public void applyGravity(List<Body> bodies) {
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
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
                double epsilon = 1e-7;
                double F = (G * A.mass * B.mass) / (Math.pow(distance, 2) + Math.pow(epsilon, 2));
                // Standard notation suggests : aAB -> acceleration of A due to force applied by B
                double accn_A_B_x = (r_x_unit * F / A.mass);
                double accn_A_B_y = (r_y_unit * F / A.mass);
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
     *
     * @param bodies List of objects of class <code>Body</code>
     *               <br>
     *  Checks for collision using the idea that for the bodies to be just touching the distance between their
     *               centres would be equal to the sum of their radii :
     *         <pre>dist = |r_1 + r_2| </pre>
     *     <br>
     *               If two bodies touch, they merge their masses and the combined mass and velocity are given by:
     *               <pre>M = m1 + m2 (Law of conservation of mass)</pre>
     *               <pre>v = (m1u1 + m2u2)/(m1+m2) (Law of conservation of linear momentum)</pre>
     *               Hence, the new body is added to {@code bodyList} and the older bodies are removed from the list.
     */
    public void checkCollision(List<Body> bodies) {
        List<Body> toRemove = new ArrayList<Body>();
        List<Body> toAdd = new ArrayList<Body>();
        if (bodies.size() >= 2) {
            for (int i = 0; i < bodies.size(); i++) {
                for (int j = i + 1; j < bodies.size(); j++) {
                    Body A = bodies.get(i);
                    Body B = bodies.get(j);
                    double distance = Math.sqrt(Math.pow(A.x - B.x, 2) + Math.pow(A.y - B.y, 2));
                    if (distance <= A.radius + B.radius) {
//                        System.out.println("Collision hath occured.");
                        double combinedMass = A.mass + B.mass;
                        double v_new_x = (A.mass * A.v_x + B.mass * B.v_x) / combinedMass;
                        double v_new_y = (A.mass * A.v_y + B.mass * B.v_y) / combinedMass;
                        double combinedRadius = Math.cbrt(Math.pow(A.radius, 3) + Math.pow(B.radius, 3));
                        Body combinedBody = new Body(A.x, A.y, v_new_x, v_new_y, combinedMass, combinedRadius, Color.PINK);
                        toAdd.add(combinedBody);
                        toRemove.add(A);
                        toRemove.add(B);
//                        System.out.println(combinedBody.v_x + " " + combinedBody.v_y);

                    }
                }
            }
            bodies.removeAll(toRemove);
            bodyList.addAll(toAdd);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { //gets called every 60th of a second
        if (!paused) {
            frameCount++;
            applyGravity(bodyList);
            bodyList.forEach(Body::update);
            checkCollision(bodyList);
        }
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        spacetimeGrid.draw(g, getWidth(), getHeight(), bodyList);
        for (Body body : bodyList) {
            body.draw(g);
        }
        if (mouseInteraction.dragging) {
            g2.setColor(Color.RED);
            g2.drawLine((int) mouseInteraction.pressX, (int) mouseInteraction.pressY, (int) mouseInteraction.currentX, (int) mouseInteraction.currentY);
        }
        drawHUD((Graphics2D) g);
        if (showHelp) {
            drawHelp(g2);
        }

    }
    // HUD and Help UI elements :
    public void drawHUD(Graphics2D g2) {
        g2.setColor(new Color(57, 97, 128, 206));
        g2.fillRoundRect(10, 10, 220, 80, 15, 15);
        g2.setColor(Color.WHITE);
        int timeElapsed = (int)(frameCount / 60);
        g2.drawString("Bodies : " + bodyList.size(), 20, 25);
        g2.drawString("Time   : " + timeElapsed + "s", 20, 45);
        g2.drawString(paused ? "[PAUSED]" : "[RUNNING]", 20, 65);
        g2.drawString("[H] to toggle Help; [ESC] to Quit", 20, 85);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void drawHelp(Graphics2D g2) {
        g2.setColor(new Color(132, 204, 95, 110));
        g2.fillRoundRect(10, 100, 220, 140, 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawString("[CONTROLS]", 20, 115);
        g2.drawString("[SPACE]  — pause / resume", 20, 135);
        g2.drawString("[R] - Reset Scene", 20, 155);
        g2.drawString("[C] - Clear Scene", 20, 175);
        g2.drawString("[H] - Toggle Help", 20, 195);
        g2.drawString("[{Drag}LMB] - Spawn Body", 20, 215);
        g2.drawString("[RMB] - Remove Body", 20, 235);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}

