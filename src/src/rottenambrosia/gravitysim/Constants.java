/**
 * And all the roads that lead you there were winding
 * And all the lights that light the way are blinding
 * There are many things that I would like to say to you
 * But I don't know how
 */

package rottenambrosia.gravitysim;

public class Constants {
    public static final double G     = 6.6743e-11;
    public boolean spawning = false;
    public static final double SPAWN_MASS = 3e12;
    public static final double SPAWN_RADIUS = 10;
    public static final double VELOCITY_SCALE = 0.25;
    public static final double C_LIGHT   = 299792458.0;  // speed of light m/s
    public static final double SPACE_SCALE = 100.0;     // 1 pixel = 1000 metres
    public static final double WARP_SCALE  = 8e5;       // metres of warp → pixels
    public static final double MAX_WARP = 999.99;  // max pixels a grid node can drop
    public static final int    GRID_COLS   = 200;         // grid columns
    public static final int    GRID_ROWS   = 200; // grid rows;
    public static final int    TRAIL_LENGTH = 40;
}
