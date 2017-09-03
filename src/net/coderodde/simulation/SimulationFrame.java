package net.coderodde.simulation;

import java.awt.Color;
import java.util.Objects;
import javax.swing.JFrame;

public final class SimulationFrame extends JFrame {
    
    private static final String FRAME_TITLE = "Closed system simulation";
    
    public SimulationFrame(SimulationCanvas simulationCanvas,
                           int width,
                           int height) {
        super(FRAME_TITLE);
        Objects.requireNonNull(simulationCanvas, "The input canvas is null.");
        setSize(width, height);
        simulationCanvas.setSize(width, height);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(simulationCanvas);
        simulationCanvas.setBackground(Color.BLACK);
        setResizable(false);
        setVisible(true);
    }
}
