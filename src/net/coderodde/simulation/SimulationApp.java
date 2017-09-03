package net.coderodde.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationApp {

    /**
     * The minimum particle mass.
     */
    private static final double MINIMUM_MASS = 5.0;
    
    /**
     * The maximum particle mass.
     */
    private static final double MAXIMUM_MASS = 30.0;
    
    /**
     * Reserve the number of pixels for the title bar.
     */
    private static final int TITLE_BAR_RESERVED_HEIGHT = 50;
    
    /**
     * The default number of particles in the simulation.
     */
    private static final int DEFAULT_PARTICLES = 3;
    
    /**
     * The time step.
     */
    private static final double TIME_STEP = 0.1;
    
    /**
     * The number of milliseconds spent between two consecutive time quants.
     */
    private static final int SLEEP_TIME = 100;
    
    /**
     * Used for randomly generating the 
     */
    private static final int COLOR_CHANNEL_MAX = 256;
    
    /**
     * The maximum initial velocity horizontally or vertically.
     */
    private static final double MAX_INITIAL_VELOCITY = 10.0;
    
    public static void main(String[] args) {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenDimension.height -= TITLE_BAR_RESERVED_HEIGHT;
        
        double worldWidth = screenDimension.width;
        double worldHeight = screenDimension.height;
        
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        
        System.out.println("Seed = " + seed);
        
        List<Particle> particles = getParticles(DEFAULT_PARTICLES, 
                                                worldWidth,
                                                worldHeight,
                                                random);
        
        SimulationCanvas simulationCanvas = new SimulationCanvas();
        
        Simulator simulator = new Simulator(particles,
                                            simulationCanvas,
                                            worldWidth,
                                            worldHeight,
                                            TIME_STEP,
                                            SLEEP_TIME);
        
        simulationCanvas.setSimulator(simulator);
        
        SimulationFrame simulationFrame = 
                new SimulationFrame(simulationCanvas,
                                    (int) worldWidth,
                                    (int) worldHeight);
        
        simulationFrame.addKeyListener(new SimulationFrameKeyListener(simulator));
        simulator.run();
    }
    
    private static List<Particle> getParticles(int particles,
                                               double worldWidth,
                                               double worldHeight,
                                               Random random) {
        List<Particle> particleList = new ArrayList<>(particles);
        
        for (int i = 0; i < particles; ++i) {
            particleList.add(createRandomParticle(random,
                                                  worldWidth,
                                                  worldHeight));
        }
        
        return particleList;
    }
    
    private static Particle createRandomParticle(Random random,
                                                 double worldWidth,
                                                 double worldHeight) {
        double mass = MINIMUM_MASS + 
                     (MAXIMUM_MASS - MINIMUM_MASS) * random.nextDouble();
        int radius = (int) mass;
        Color color = new Color(random.nextInt(COLOR_CHANNEL_MAX), 
                                random.nextInt(COLOR_CHANNEL_MAX),
                                random.nextInt(COLOR_CHANNEL_MAX));
        
        Particle particle = new Particle(mass, radius, color);
        
        particle.setX(worldWidth * random.nextDouble());
        particle.setY(worldHeight * random.nextDouble());
        particle.setVelocityX(MAX_INITIAL_VELOCITY * random.nextDouble());
        particle.setVelocityY(MAX_INITIAL_VELOCITY * random.nextDouble());
        
        return particle;
    }
}
