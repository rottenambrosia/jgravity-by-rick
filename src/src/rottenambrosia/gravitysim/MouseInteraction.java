/**
 * What she asked of me at the end of the day
 * Caligula would have blushed
 * "Oh, you've been in the house too long," she said
 * And I naturally fled
 **/

package rottenambrosia.gravitysim;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.List;
import static rottenambrosia.gravitysim.Constants.*;


public class MouseInteraction implements MouseListener {

    double pressX, pressY, releaseX, releaseY, d_x, d_y, v_x, v_y;
    List<Body> bodyList;
    public MouseInteraction(List<Body> bodyList) {
        this.bodyList = bodyList;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            pressX = e.getX();
            pressY = e.getY();
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            Body closest = null;
            double minDist = Double.MAX_VALUE;
            for (Body body : bodyList) {
                double distance = Math.sqrt(Math.pow(body.x-pressX, 2) + Math.pow(body.y-pressY, 2));
                if (distance<=minDist) {
                    minDist = distance;
                    closest = body;
                }
            } bodyList.remove(closest);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            releaseX = e.getX();
            releaseY = e.getY();
            d_x = releaseX-pressX;
            d_y = releaseY-pressY;
            v_x = d_x*VELOCITY_SCALE;
            v_y = d_y*VELOCITY_SCALE;
            Color[] colorArray = {Color.WHITE, Color.CYAN, Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE};
            Body body = new Body(
                    pressX, pressY,
                    v_x, v_y,
                    SPAWN_MASS, SPAWN_RADIUS,
                    colorArray[new Random().nextInt(colorArray.length)]
            );
            bodyList.add(body);
            System.out.println("Body spawned.");
        } else {
            System.out.println("That does nothing for me.");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
