package net.coderodde.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import static net.coderodde.simulation.Configuration.DEFAULT_NUMBER_OF_PARTICLES;
import static net.coderodde.simulation.Configuration.MAXIMUM_PARTICLE_MASS;
import static net.coderodde.simulation.Configuration.MAX_INITIAL_VELOCITY;
import static net.coderodde.simulation.Configuration.MINIMUM_PARTICLE_MASS;
import static net.coderodde.simulation.Configuration.PIXELS_PER_UNIT_LENGTH;
import static net.coderodde.simulation.Configuration.SLEEP_TIME;
import static net.coderodde.simulation.Configuration.TIME_STEP;

/**
 * This class implements the entire simulation program.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class SimulationApp {

    /**
     * Reserve the number of pixels for the title bar.
     */
    private static final int TITLE_BAR_RESERVED_HEIGHT = 50;

    /**
     * Used for randomly generating the color components.
     */
    private static final int COLOR_CHANNEL_MAX = 256;

    /**
     * Defines the entry point of the program.
     * 
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenDimension.height -= TITLE_BAR_RESERVED_HEIGHT;

        double worldWidth = (1.0 * screenDimension.width) 
                                 / PIXELS_PER_UNIT_LENGTH;

        double worldHeight = (1.0 * screenDimension.height)
                                  / PIXELS_PER_UNIT_LENGTH;

        long seed = System.currentTimeMillis();
        Random random = new Random(seed);

        System.out.println("Seed = " + seed);

        List<ParticleData> particleData = 
                getParticles(DEFAULT_NUMBER_OF_PARTICLES, 
                             worldWidth,
                             worldHeight,
                             random);

        SimulationPanel simulationPanel = new SimulationPanel();
        List<Particle> particles = extractParticles(particleData);
        List<ParticleRenderer> particleRenderers = 
                extractRenderers(particleData);
        
        ParticlePairForce particlePairForce = new DefaultParticlePairForce();
        ParticlePairPotentialEnergy particlePairPotentialEnergy =
                new RepellingParticlePairPotentialEnergy();
        
        SimulationEngine simulator = new SimulationEngine(particles,
                                            particlePairForce,
                                            particlePairPotentialEnergy,
                                            simulationPanel,
                                            worldWidth,
                                            worldHeight,
                                            TIME_STEP,
                                            SLEEP_TIME);

        simulationPanel.setParticleRenderers(particleRenderers);
        simulationPanel.setSimulator(simulator);
        SimulationFrame simulationFrame = 
                new SimulationFrame(simulationPanel,
                                    screenDimension.width,
                                    screenDimension.height);

        SimulationFrameKeyListener keyListener = 
                new SimulationFrameKeyListener(simulator);

        simulationFrame.addKeyListener(keyListener);
        simulator.run();
    }
    
    private static List<Particle> extractParticles(List<ParticleData> data) {
        return data.stream()
                   .map((datum) -> datum.particle)
                   .collect(Collectors.toList());
    }
    
    private static List<ParticleRenderer> 
        extractRenderers(List<ParticleData> data) {
        return data.stream()
                   .map((datum) -> datum.particleRenderer)
                   .collect(Collectors.toList());
    }

    private static List<ParticleData> getParticles(int particles,
                                                   double worldWidth,
                                                   double worldHeight,
                                                   Random random) {
        List<ParticleData> particleList = new ArrayList<>(particles);

        for (int i = 0; i < particles; ++i) {
            particleList.add(createRandomParticle(random,
                                                  worldWidth,
                                                  worldHeight));
        }

        return particleList;
    }

    private static final class ParticleData {
        Particle particle;
        ParticleRenderer particleRenderer;
        
        ParticleData(Particle particle, ParticleRenderer particleRenderer) {
            this.particle = particle;
            this.particleRenderer = particleRenderer;
        }
    }
    
    private static ParticleData createRandomParticle(Random random,
                                                     double worldWidth,
                                                     double worldHeight) {
        double mass = MINIMUM_PARTICLE_MASS + 
                     (MAXIMUM_PARTICLE_MASS - MINIMUM_PARTICLE_MASS) * 
                      random.nextDouble();
        
        int radius = (int) mass;
        Color color = new Color(random.nextInt(COLOR_CHANNEL_MAX), 
                                random.nextInt(COLOR_CHANNEL_MAX),
                                random.nextInt(COLOR_CHANNEL_MAX));

        
        Particle particle = new Particle(mass);
        particle.setX(worldWidth  * random.nextDouble());
        particle.setY(worldHeight * random.nextDouble());
        particle.setVelocityX(MAX_INITIAL_VELOCITY * random.nextDouble());
        particle.setVelocityY(MAX_INITIAL_VELOCITY * random.nextDouble());
        
        ParticleRenderer particleRenderer = new ParticleRenderer(particle,
                                                                 radius,
                                                                 color);
        return new ParticleData(particle, particleRenderer);
    }
}
