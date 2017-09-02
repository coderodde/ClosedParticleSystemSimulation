package net.coderodde.simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public final class Simulator {

    private final List<Particle> particles = new ArrayList<>();
    
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
    
    public Simulator(List<Particle> particles,
                     double worldWidth,
                     double worldHeight,
                     double timeStep,
                     int sleepTime) {
        Objects.requireNonNull(particles, "The particle list is null.");
        checkNotEmpty(particles);
        copy(particles);
        checkParticlesDoNotOverlap();
        this.worldWidth = checkWorldWidth(worldWidth);
        this.worldHeight = checkWorldHeight(worldHeight);
        this.timeStep = checkTimeStep(timeStep);
        this.sleepTime = checkSleepTime(sleepTime);
        totalEnergy = computeTotalEnergy();
    }
    
    public void step() {
        
    }
    
    List<Particle> getParticles() {
        return Collections.<Particle>unmodifiableList(particles);
    }
    
    private double computeTotalEnergy() {
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
                timeStep,
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
}
