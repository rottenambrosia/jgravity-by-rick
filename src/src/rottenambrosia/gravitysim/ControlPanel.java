/**
 * No one tried
 * To read my eyes
 * No one but you
 * Wish it weren't true
 */

package rottenambrosia.gravitysim;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class ControlPanel extends JPanel {

    public ControlPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(240, 0));

        add(makeSlider(
                "Timestep dt",
                1, 20,
                (int) (Constants.dt * 10),
                val -> Constants.dt = val / 10.0,
                () -> String.format("%.1f", Constants.dt)
        ));

        add(makeSlider(
                "Softening",
                1, 50,
                (int) Constants.SOFTENING,
                val -> Constants.SOFTENING = val,
                () -> String.format("%.0f", Constants.SOFTENING)
        ));

        add(makeSlider(
                "Velocity Scale",
                1, 20,
                (int) (Constants.VELOCITY_SCALE * 100),
                val -> Constants.VELOCITY_SCALE = val / 100.0,
                () -> String.format("%.2f", Constants.VELOCITY_SCALE)
        ));

        add(makeSlider(
                "Spawn Mass",
                1, 100,
                (int) (Constants.SPAWN_MASS / 1e12),
                val -> Constants.SPAWN_MASS = val * 1e-12,
                () -> String.format("%.0f", Constants.SPAWN_MASS)
        ));
    }

    private JPanel makeSlider(
            String label,
            int min,
            int max,
            int initial,
            IntConsumer onChange,
            Supplier<String> displayText
    ) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBackground(new Color(20, 20, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80)),
                label,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                null,
                Color.LIGHT_GRAY
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JSlider slider = new JSlider(min, max, initial);
        slider.setBackground(new Color(20, 20, 30));
        slider.setForeground(Color.CYAN);

        JLabel valueLabel = new JLabel();
        valueLabel.setForeground(Color.CYAN);
        valueLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));

        slider.addChangeListener(e -> {
            onChange.accept(slider.getValue());
            valueLabel.setText(displayText.get());
        });

//        onChange.accept(initial);
        valueLabel.setText(displayText.get());

        panel.add(slider, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }
}

