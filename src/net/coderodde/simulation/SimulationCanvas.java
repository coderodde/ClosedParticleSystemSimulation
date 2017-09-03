package net.coderodde.simulation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a simple canvas for drawing the simulated particle 
 * system.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class SimulationCanvas extends Canvas {

    /**
     * The list of particles.
     */
    private List<Particle> particles;
    
    /**
     * The simulation engine.
     */
    private Simulator simulator;
    
    @Override
    public void paint(Graphics g) {
        update(g);
    }
    
    @Override
    public void update(Graphics g) {
        double totalEnergy = simulator.computeTotalEnergy();
        String totalEnergyString = "Total energy: " + totalEnergy;
        
        g.setColor(getBackground());
        g.clearRect(0, 0, getWidth(), getHeight());
        
        for (Particle particle : particles) {
            particle.draw(g);
        }
        
        g.setColor(Color.WHITE);
        g.drawChars(totalEnergyString.toCharArray(), 
                    0, 
                    totalEnergyString.length(),
                    0,
                    20);
    }
    
    void setParticles(List<Particle> particles) {
        this.particles = Objects.requireNonNull(
                particles, 
                "The particle list is null.");
    }
    
    void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
