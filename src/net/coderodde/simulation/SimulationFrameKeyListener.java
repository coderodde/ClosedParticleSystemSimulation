package net.coderodde.simulation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public final class SimulationFrameKeyListener implements KeyListener {

    private final SimulationEngine simulator;

    public SimulationFrameKeyListener(SimulationEngine simulator) {
        this.simulator = Objects.requireNonNull(simulator,
                                                "The simulator is null.");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        simulator.togglePause();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
