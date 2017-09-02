package net.coderodde.simulation;

import java.awt.Color;
import java.util.Objects;
import javax.swing.JFrame;

public final class SimulationFrame {
    
    private static final String FRAME_TITLE = "Closed system simulation";
    private final JFrame frame = new JFrame(FRAME_TITLE);
    
    public SimulationFrame(SimulationCanvas simulationCanvas,
                           int width,
                           int height) {
        Objects.requireNonNull(simulationCanvas, "The input canvas is null.");
        frame.setSize(width, height);
        simulationCanvas.setSize(width, height);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(simulationCanvas);
        simulationCanvas.setBackground(Color.BLACK);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public JFrame getFrame() {
        return frame;
    }
}
