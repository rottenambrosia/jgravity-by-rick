/**
 * And all the roads that lead you there were winding
 * And all the lights that light the way are blinding
 * There are many things that I would like to say to you
 * But I don't know how
 */

package rottenambrosia.gravitysim;

public class Constants {
    public static double G     = 6.6743e-11;
    public static double SPAWN_MASS = 3e12;
    public static double SPAWN_RADIUS = 10;
    public static double VELOCITY_SCALE = 0.10;
    public static double C_LIGHT   = 299792458.0;  // speed of light m/s
    public static double SPACE_SCALE = 100.0;     // 1 pixel = 1000 metres
    public static double WARP_SCALE  = 5e6;       // metres of warp → pixels
    public static double MAX_WARP = 999.99;  // max pixels a grid node can drop
    public static int    GRID_COLS   = 200;         // grid columns
    public static int    GRID_ROWS   = 200; // grid rows;
    public static int    TRAIL_LENGTH = 40;
    public static double SOFTENING = 10.0;
    public static double dt = 0.5;
}
