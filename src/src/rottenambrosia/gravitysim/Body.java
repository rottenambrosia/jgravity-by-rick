/**
 * I have to tell you just how I feel
 * I won't share you with another boy
 * I know my mind is made up
 * So put away your makeup
 */

package rottenambrosia.gravitysim;

import javax.swing.*;
import java.awt.*;

public class Body {
    public double x, y;
    public double v_x, v_y;
    public double mass;
    public double radius;
    public Color color;
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
        x = x + v_x;
        y = y + v_y;
    }
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)(x-radius), (int)(y-radius), (int)radius*2, (int)radius*2);
    }
}
