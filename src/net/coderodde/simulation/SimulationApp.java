package net.coderodde.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationApp {

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
     * The maximum mass of a particle.
     */
    private static final double MAX_PARTICLE_MASS = 10.0;
    
    /**
     * The maximum particle radius in pixels.
     */
    private static final int MAX_PARTICLE_RADIUS = 20;
    
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
        
        Simulator simulator = new Simulator(particles,
                                            worldWidth,
                                            worldHeight,
                                            TIME_STEP,
                                            SLEEP_TIME);
        
        SimulationCanvas simulationCanvas = 
                new SimulationCanvas(simulator.getParticles());
        
        SimulationFrame simulationFrame = 
                new SimulationFrame(simulationCanvas,
                                    (int) worldWidth,
                                    (int) worldHeight);
        
        simulationFrame.getFrame()
                       .addKeyListener(
                               new SimulationFrameKeyListener(simulator));
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
        double mass = MAX_PARTICLE_MASS * random.nextDouble();
        int radius = random.nextInt(MAX_PARTICLE_RADIUS) + 1;
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
