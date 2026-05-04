/**
 * I have to tell you just how I feel
 * I won't share you with another boy
 * I know my mind is made up
 * So put away your makeup
 */

package rottenambrosia.gravitysim;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;

import static rottenambrosia.gravitysim.Constants.TRAIL_LENGTH;

public class Body {
    public double x, y;
    public double v_x, v_y;
    public double mass;
    public double radius;
    public Color color;
    public Deque<Point2D.Double> trail = new ArrayDeque<>();

    public Body(double x, double y, double v_x, double v_y, double mass, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.v_x = v_x;
        this.v_y = v_y;
        this.mass = mass;
        this.radius = radius;
        this.color = color;
    }
    public void update () {
        trail.addFirst(new Point2D.Double(x, y));
        if (trail.size() > TRAIL_LENGTH) {
            trail.pollLast();
        }
        x = x + v_x;
        y = y + v_y;
    }
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int i = 0;
        for (Point2D.Double point : trail) {
            double alpha = 1.0 - (double) i / TRAIL_LENGTH;
            int r = (int) (radius*alpha*0.6);
            if (r<1) {
                i++;
                continue;
            }
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                    (int)(alpha * 120)));
            g2.fillOval((int)(point.x - r), (int)(point.y - r), r*2, r*2);

            i++;
        }
        g.setColor(color);
        g.fillOval((int)(x-radius), (int)(y-radius), (int)radius*2, (int)radius*2);
    }
}
