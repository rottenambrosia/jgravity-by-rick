/**
 * Take a listen to your spirit
 * It's cryin' out loud
 * Trying to believe
 * Oh, you say you love me, but you don't know
 */

package rottenambrosia.gravitysim;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gravity Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            SimulationPanel simulationPanel = new SimulationPanel();
            ControlPanel controlPanel = new ControlPanel();

            frame.add(simulationPanel, BorderLayout.CENTER);

            frame.add(controlPanel, BorderLayout.EAST);

            frame.setSize(1920, 1080);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            simulationPanel.requestFocusInWindow();
        });
    }
}