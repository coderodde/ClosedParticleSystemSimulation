package net.coderodde.simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements the actual simulator.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class Simulator {

    /**
     * The list of particles.
     */
    private final List<Particle> particles = new ArrayList<>();
    
    /**
     * Holds the canvas for drawing the system.
     */
    private final SimulationCanvas simulationCanvas;
    
    /**
     * The time quant.
     */
    private final double timeStep;
    
    /**
     * The total energy of the simulated system.
     */
    private final double totalEnergy;
    
    /**
     * The width of the system.
     */
    private final double worldWidth;
    
    /**
     * The height of the system.
     */
    private final double worldHeight;
    
    /**
     * Number of milliseconds between two time quants.
     */
    private final int sleepTime;
    
    /**
     * The exit flag
     */
    private volatile boolean exit = false;
    
    /**
     * The pause flag.
     */
    private volatile boolean pause = true;
    
    /**
     * Used for mapping the particles to their respective force vectors.
     */
    private final Map<Particle, Vector> particleToForceVectorMap = 
            new HashMap<>();
    
    public Simulator(List<Particle> particles,
                     SimulationCanvas simulationCanvas,
                     double worldWidth,
                     double worldHeight,
                     double timeStep,
                     int sleepTime) {
        Objects.requireNonNull(particles, "The particle list is null.");
        
        this.simulationCanvas = 
                Objects.requireNonNull(
                        simulationCanvas,
                        "The simulation canvas is null.");
        
        checkNotEmpty(particles);
        copy(particles);
        checkParticlesDoNotOverlap();
        this.worldWidth = checkWorldWidth(worldWidth);
        this.worldHeight = checkWorldHeight(worldHeight);
        this.timeStep = checkTimeStep(timeStep);
        this.sleepTime = checkSleepTime(sleepTime);
        totalEnergy = computeTotalEnergy();
        simulationCanvas.setParticles(this.particles);
    }
    
    public void togglePause() {
        pause = !pause;
    }
    
    public void run() {
        while (!exit) {
            if (!pause) {
                makeStep();
                System.out.println("fdsa");
                simulationCanvas.repaint();
            }
            
            sleep(sleepTime);
        }
    }
    
    List<Particle> getParticles() {
        return Collections.<Particle>unmodifiableList(particles);
    }
    
    private void makeStep() {
        // Compute the force vectors of all partices:
        computeForceVectors();
        updateParticleVelocities();
        moveParticles();
        computePotentialEnergyDelta();
        resolveWorldBorderCollisions();
        particleToForceVectorMap.clear();
    }
    
    private void computePotentialEnergyDelta() {
        double currentPotentialEnergy = computeTotalEnergy();
        double potentialEnergyDelta = currentPotentialEnergy - totalEnergy;
        System.out.println("Delta: " + potentialEnergyDelta);
    }
    
    private void resolveWorldBorderCollisions() {
        for (Particle particle : particles) {
            if (particle.getY() <= 0.0 || particle.getY() >= worldHeight) {
                particle.setVelocityY(-particle.getVelocityY());
            } 
            
            if (particle.getX() <= 0.0 || particle.getX() >= worldWidth) {
                particle.setVelocityX(-particle.getVelocityX());
            }
        }
    }
    
    private void moveParticles() {
        for (Particle particle : particles) {
            particle.setX(particle.getX() + particle.getVelocityX() * timeStep);
            particle.setY(particle.getY() + particle.getVelocityY() * timeStep);
        }
    }
    
    private void updateParticleVelocities() {
        for (Map.Entry<Particle, Vector> e
                : particleToForceVectorMap.entrySet()) {
            Particle particle = e.getKey();
            Vector vector = e.getValue();
            vector = vector.multiply(1.0 / particle.getMass());
            
            particle.setVelocityX(
                    particle.getVelocityX() + vector.getX() * timeStep);
            
            particle.setVelocityY(
                    particle.getVelocityY() + vector.getY() * timeStep);
        }
    }
    
    private void computeForceVectors() {
        for (Particle particle : particles) {
            Vector vector = new Vector();
            
            for (Particle other : particles) {
                if (particle == other) {
                    // Do not compute the force from and to itself.
                    continue;
                }
                
                Vector aux = computeForceVector(particle, other);
                vector = vector.plus(aux);
            }
            
            particleToForceVectorMap.put(particle, vector);
        }
    }
    
    private Vector computeForceVector(Particle target, Particle other) {
        double vectorLength = target.getRejectionForce(other);
        double dx = target.getX() - other.getX();
        double dy = other.getY() - target.getY();
        double angle = Math.atan2(dy, dx);
        double xComponent = vectorLength * Math.cos(angle);
        double yComponent = vectorLength * Math.sin(angle);
        return new Vector(xComponent, yComponent);
    }
    
    public double computeTotalEnergy() {
        double totalEnergy = 0.0;
        
        for (Particle particle : particles) {
            totalEnergy += particle.getKineticEnergy();
        }
        
        for (int i = 0; i < particles.size(); ++i) {
            Particle particle1 = particles.get(i);
            
            for (int j = i + 1; j < particles.size(); ++j) {
                Particle particle2 = particles.get(j);
                totalEnergy += particle1.getPotentialEnergy(particle2);
            }
        }
        
        return totalEnergy;
    }
    
    private void checkNotEmpty(List<Particle> particles) {
        if (particles.isEmpty()) {
            throw new IllegalArgumentException("No particles given.");
        }
    }
    
    private void copy(List<Particle> particles) {
        for (Particle particle : particles) {
            this.particles.add(new Particle(particle));
        }
    }
    
    private void checkParticlesDoNotOverlap() {
        for (int i = 0; i < particles.size(); ++i) {
            Particle particle1 = particles.get(i);
            
            for (int j = i + 1; j < particles.size(); ++j) {
                Particle particle2 = particles.get(j);
                
                if (particle1.getX() == particle2.getX()
                        && particle1.getY() == particle2.getY()) {
                    throw new IllegalStateException(
                            "Two particles occupy the same spot.");
                }
            }
        }
    }
    
    private double checkTimeStep(double timeStep) {
        if (Double.isNaN(timeStep)) {
            throw new IllegalArgumentException("The time step is NaN.");
        }
        
        if (timeStep <= 0.0) {
            throw new IllegalArgumentException(
                    "The time step is non-positive: " + timeStep + ".");
        }
        
        if (Double.isInfinite(timeStep)) {
            throw new IllegalArgumentException("The time step is infinite.");
        }
        
        return timeStep;
    }
    
    private double checkWorldWidth(double worldWidth) {
        return checkWorldDimension(
                worldWidth,
                "The world width is NaN.",
                "The world width is non-positive: " + worldWidth,
                "The world width is infinite.");
    }
    
    private double checkWorldHeight(double worldHeight) {
        return checkWorldDimension(
                worldHeight,
                "The world height is NaN.",
                "The world height is non-positive: " + worldHeight,
                "The world height is infinite.");
    }
    
    private double checkWorldDimension(double dimension, 
                                       String errorMessageNaN,
                                       String errorMessageNonPositive,
                                       String errorMessageInfinite) {
        if (Double.isNaN(dimension)) {
            throw new IllegalArgumentException(errorMessageNaN);
        }
        
        if (dimension <= 0.0) {
            throw new IllegalArgumentException(errorMessageNonPositive);
        }
        
        if (Double.isInfinite(dimension)) {
            throw new IllegalArgumentException(errorMessageInfinite);
        }
        
        return dimension;
    }
    
    private int checkSleepTime(int sleepTime) {
        if (sleepTime < 1) {
            throw new IllegalArgumentException(
                    "The sleep time is non-positive: " + sleepTime + ".");
        }
        
        return sleepTime;
    }
    
    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            
        }
    }
}
