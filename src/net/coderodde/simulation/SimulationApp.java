package net.coderodde.simulation;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationApp {

    private static final int TITLE_BAR_RESERVED_HEIGHT = 50;
    private static final int DEFAULT_PARTICLES = 3;
    private static final double TIME_STEP = 0.1;
    
    public static void main(String[] args) {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenDimension.height -= TITLE_BAR_RESERVED_HEIGHT;
        
        double worldWidth = screenDimension.width;
        double worldHeight = screenDimension.height;
        
        Random random = new Random();
        
        List<Particle> particle = getParticles(DEFAULT_PARTICLES, 
                                               worldWidth,
                                               worldHeight,
                                               random);
        
        Simulator simulator = new Simulator();
        SimulationCanvas simulationCanvas = new SimulationCanvas();
        SimulationFrame simulationFrame = new SimulationFrame();
    }
    
    private static List<Particle> getParticles(int particles,
                                               double worldWidth,
                                               double worldHeight,
                                               Random random) {
        List<Particle> particleList = new ArrayList<>(particles);
        
        for (int i = 0; i < particles; ++i) {
            particleList.add(createRandomParticles(random));
        }
        
        return particleList;
    }
}
