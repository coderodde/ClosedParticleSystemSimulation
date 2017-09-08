package net.coderodde.simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static net.coderodde.simulation.Utils.checkNonInfinite;
import static net.coderodde.simulation.Utils.checkNonNaN;
import static net.coderodde.simulation.Utils.checkNonNegative;

/**
 * This class implements the actual simulator.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class SimulationEngine {

    /**
     * The list of particles.
     */
    private final List<Particle> particles = new ArrayList<>();
    
    /**
     * Holds the object computing the force between two particles.
     */
    private final ParticlePairForce particlePairForce;
    
    /**
     * Holds the object computing the potential energy between two particles.
     */
    private final ParticlePairPotentialEnergy particlePairPotentialEnergy;

    /**
     * Holds the canvas for drawing the system.
     */
    private final SimulationPanel simulationPanel;

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

    public SimulationEngine(List<Particle> particles,
                     ParticlePairForce particlePairForce,
                     ParticlePairPotentialEnergy particlePairPotentialEnergy,
                     SimulationPanel simulationCanvas,
                     double worldWidth,
                     double worldHeight,
                     double timeStep,
                     int sleepTime) {
        Objects.requireNonNull(particles, "The particle list is null.");
        this.particlePairForce = 
                Objects.requireNonNull(
                        particlePairForce, 
                        "The particle pair force is null.");
        this.particlePairPotentialEnergy = 
                Objects.requireNonNull(
                        particlePairPotentialEnergy,
                        "The particle pair potential energy is null.");
        
        checkNotEmpty(particles);
        checkParticlesDoNotOverlap(particles);
        
        this.particles.addAll(particles);
        this.simulationPanel = 
                Objects.requireNonNull(
                        simulationCanvas,
                        "The simulation canvas is null.");

        this.worldWidth = checkWorldWidth(worldWidth);
        this.worldHeight = checkWorldHeight(worldHeight);
        this.timeStep = checkTimeStep(timeStep);
        this.sleepTime = checkSleepTime(sleepTime);
        totalEnergy = computeTotalEnergy();
    }

    public void togglePause() {
        pause = !pause;
    }

    public void run() {
        while (!exit) {
            if (!pause) {
                performStep();
                simulationPanel.repaint();
            }

            sleep(sleepTime);
        }
    }

    List<Particle> getParticles() {
        return Collections.<Particle>unmodifiableList(particles);
    }

    /**
     * Checks that the particle list is not empty.
     * 
     * @param particles the particles list.
     */
    private void checkNotEmpty(List<Particle> particles) {
        if (particles.isEmpty()) {
            throw new IllegalArgumentException("No particles given.");
        }
    }

    /**
     * Performs one simulation step.
     */
    private void performStep() {
        // Compute the force vectors of all partices:
        computeForceVectors();
        updateParticleVelocities();
        moveParticles();
        resolveWorldBorderCollisions();
        normalizeVelocityVectors();
        particleToForceVectorMap.clear();
    }

    /**
     * Computes all the repelling force vectors for each particle.
     */
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

    /**
     * Computes a repelling force vector from {@code other} to {@code target}.
     * 
     * @param target the target particle.
     * @param other  the particle exerting repelling force towards 
     *               {@code target}.
     * @return the force vector.
     */
    private Vector computeForceVector(Particle target, Particle other) {
        double vectorLength = particlePairForce.getForce(target, other);
        double dx = target.getX() - other.getX();
        double dy = target.getY() - other.getY();
        double angle = Math.atan2(dy, dx);
        double xComponent = vectorLength * Math.cos(angle);
        double yComponent = vectorLength * Math.sin(angle);
        return new Vector(xComponent, yComponent);
    }

    /**
     * Updates the velocities of each particle.
     */
    private void updateParticleVelocities() {
        for (Map.Entry<Particle, Vector> e
                : particleToForceVectorMap.entrySet()) {
            Particle particle = e.getKey();
            Vector vector = e.getValue();
            // Make the force 'vector' a acceleration vector:
            vector = vector.multiply(1.0 / particle.getMass());

            // Update the velocity components:
            particle.setVelocityX(
                    particle.getVelocityX() + vector.getX() * timeStep);

            particle.setVelocityY(
                    particle.getVelocityY() + vector.getY() * timeStep);
        }
    }

    /**
     * Moves all the particles.
     */
    private void moveParticles() {
        for (Particle particle : particles) {
            particle.setX(particle.getX() + particle.getVelocityX() * timeStep);
            particle.setY(particle.getY() + particle.getVelocityY() * timeStep);
        }
    }

    /**
     * Resolves all the border collisions.
     */
    private void resolveWorldBorderCollisions() {
        for (Particle particle : particles) {
            if (particle.getY() - particle.getRadius() <= 0.0) {
                particle.setVelocityY(-particle.getVelocityY());
                particle.setY(particle.getRadius());
            } else if (particle.getY() + particle.getRadius() >= worldHeight) {
                particle.setVelocityY(-particle.getVelocityY());
                particle.setY(worldHeight - particle.getRadius());
            }
            
            if (particle.getX() - particle.getRadius() <= 0.0) {
                particle.setVelocityX(particle.getVelocityX());
                particle.setX(particle.getRadius());
            } else if (particle.getX() + particle.getRadius() >= worldWidth) {
                particle.setVelocityX(particle.getVelocityX());
                particle.setX(worldWidth - particle.getRadius());
            }
        }
    }

    /**
     * Normalizes the current velocity vectors such that the total energy of the
     * system remains constant.
     */
    private void normalizeVelocityVectors() {
        double totalEnergyDelta = computeTotalEnergyDelta();
        double factor = getNormalizationFactor(totalEnergyDelta);

        for (Particle particle : particles) {
            particle.setVelocityX(factor * particle.getVelocityX());
            particle.setVelocityY(factor * particle.getVelocityY());
        }
    }

    /**
     * Computes the difference between initial total energy and current total
     * energy.
     * 
     * @return the total energy difference.
     */
    private double computeTotalEnergyDelta() {
        double currentTotalEnergy = computeTotalEnergy();
        double totalEnergyDelta = totalEnergy - currentTotalEnergy;
        return totalEnergyDelta;
    }

    /**
     * Computes such a velocity normalization constant, that the total energy of
     * the system remains constant.
     * 
     * @param totalEnergyDelta the difference of initial and current total
     *                         energies.
     * @return the velocity normalization constant.
     */
    private double getNormalizationFactor(double totalEnergyDelta) {
        double aux = totalEnergyDelta / computeTotalKineticEnergy() + 1;

        if (aux < 0.0) {
            // Does not seem to happen any more but you never know..
            System.err.println("Cannot compute normalization constant. " +
                               "aux = " + aux);
            return 1.0;
        }

        return Math.sqrt(aux);
    }

    /**
     * Computes the sum of kinetic energies of all the particles.
     * 
     * @return the sum of kinetic energies.
     */
    private double computeTotalKineticEnergy() {
        double kineticEnergy = 0.0;

        for (Particle particle : particles) {
            kineticEnergy += particle.getKineticEnergy();
        }

        return kineticEnergy;
    }

    /**
     * Computes the current total energy.
     * 
     * @return the current total energy.
     */
    public double computeTotalEnergy() {
        double totalEnergy = 0.0;

        for (Particle particle : particles) {
            totalEnergy += particle.getKineticEnergy();
        }

        for (int i = 0; i < particles.size(); ++i) {
            Particle particle1 = particles.get(i);

            for (int j = i + 1; j < particles.size(); ++j) {
                Particle particle2 = particles.get(j);
                totalEnergy += 
                        particlePairPotentialEnergy
                                .getPotentialEnergy(particle1, particle2);
            }
        }

        return totalEnergy;
    }

    /**
     * Checks that there is no two different particles on the same spot.
     */
    private void checkParticlesDoNotOverlap(List<Particle> particles) {
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
        checkNonNaN(timeStep, "The time step is NaN.");
        checkNonNegative(timeStep,
                         "The time step is non-positive: " + timeStep + ".");
        checkNonInfinite(timeStep, "The time step is infinite.");
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
        checkNonNaN(dimension, errorMessageNaN);
        checkNonNegative(dimension, errorMessageNonPositive);
        checkNonInfinite(dimension, errorMessageInfinite);
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
