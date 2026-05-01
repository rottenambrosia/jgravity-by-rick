package rottenambrosia.gravitysim;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gravity Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            SimulationPanel simulationPanel = new SimulationPanel();
            frame.add(simulationPanel);
            frame.setVisible(true);
        });
    }
}