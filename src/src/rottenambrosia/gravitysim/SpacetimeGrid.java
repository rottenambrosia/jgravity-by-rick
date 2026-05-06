/**
 * Why pamper life's complexity
 * When leather runs smooth on the passenger seat?
 * I would go out tonight, but I haven't got a stitch to wear
 * This man said, "It's gruesome that someone so handsome should care."
 */

package rottenambrosia.gravitysim;

import java.awt.*;
import java.util.List;
import static rottenambrosia.gravitysim.Constants.*;

public class SpacetimeGrid {

    /**
     * <h1>Compute Warp</h1>
     * <p>Computes the warp displacement (in pixels) at screen point (px, py)
     * due to all bodies in the list.</p>
     * <br>
     * @param px x-coordinates
     * @param py y-coordinates
     * <div>
     * <br>
     * For each body:
     *   <li>1. Computes pixel distance from (px, py) to body centre</li>
     *   <li>2. Converts to metres: distM = distPx * SPACE_SCALE</li>
     *   <li>3. Computes Schwarzschild radius: rs = 2 * G * body.mass / (C_LIGHT * C_LIGHT)</li>
     *   <li>4. If distM > rs: adds 2 * sqrt(rs * (distM - rs)) to total</li></div>
     * <br>
     * @return  total * WARP_SCALE to convert to pixels.
     */
    public double computeWarp(double px, double py, List<Body> bodies) {
        double total = 0.0;
        for (Body body : bodies) {
            double distPx = Math.sqrt(Math.pow(px - body.x, 2) + Math.pow(py - body.y, 2));
            double distM = distPx * SPACE_SCALE; // Schwarzschild Radius
            double rs = (2 * G * body.mass) / (C_LIGHT*C_LIGHT);
            if (distM > rs) {
                total += 2*Math.sqrt(rs*(distM-rs)); // Flamm's Paraboloid
            }
        }
        return Math.min(total*WARP_SCALE, MAX_WARP);
    }

    /**
     * <h2>Draws the warped spacetime grid onto Graphics g.</h2>
     * <p>
     * <h3>Steps:</h3>
     * <li>Compute stepX = panelWidth  / GRID_COLS <br>
     *       Compute stepY = panelHeight / GRID_ROWS</li>
     *<li>Build a 2D array warpedY[col][row] — for each grid intersection,
     * store: baseY + computeWarp(baseX, baseY, bodies)
     * where baseX = col * stepX, baseY = row * stepY</li>
     *<li>Draw horizontal lines: for each row, connect
     * (col*stepX, warpedY[col][row]) → ((col+1)*stepX, warpedY[col+1][row])</li>
     * <li>Draw vertical lines: for each col, connect
     * (col*stepX, warpedY[col][row]) → (col*stepX, warpedY[col][row+1])</li>
     * <li>Colour: new Color(30, 120, 255, 80) — dim blue, slightly transparent</li>
     */
//    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight, List<Body> bodies) {
        Graphics2D g2 = (Graphics2D) g;
        double stepX = panelWidth / (double) GRID_COLS;
        double stepY = panelHeight / (double) GRID_ROWS;
        double[][] warpedY = new double[GRID_COLS + 1][GRID_ROWS + 1];
        for (int i = 0; i <= GRID_COLS; i++) {
            for (int j = 0; j <= GRID_ROWS; j++) {
                double baseX = i * stepX;
                double baseY = j * stepY;
                warpedY[i][j] = baseY + computeWarp(baseX, baseY, bodies);

            }
        }
        for (int i = 0; i < GRID_COLS; i++) {
            for (int j = 0; j < GRID_ROWS; j++) {
                double warpDepth = warpedY[i][j] - (j*stepY);
//                int alpha = (int)(60 + warpDepth*1.5);
                g2.setColor(new Color(20, 200, 255, 50));

                g2.drawLine((int)(i*stepX), (int)warpedY[i][j],
                        (int)((i+1)*stepX), (int)warpedY[i+1][j]);
                g2.drawLine((int)(i*stepX), (int)warpedY[i][j],
                        (int)(i*stepX), (int)warpedY[i][j+1]);
            }
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}