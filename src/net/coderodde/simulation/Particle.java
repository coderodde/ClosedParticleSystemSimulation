package net.coderodde.simulation;

import static net.coderodde.simulation.Configuration.FORCE_CONSTANT;
import static net.coderodde.simulation.Utils.checkNonInfinite;
import static net.coderodde.simulation.Utils.checkNonNaN;
import static net.coderodde.simulation.Utils.checkNonNegative;

/**
 * This class defines a particle in the simulation. The entire weight of a
 * particle is considered to be fully focused in the center of this particle.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class Particle {

    private final double mass;
    private double x;
    private double y;

    /**
     * The current velocity to the right. May be negative when the particle 
     * moves to the left.
     */
    private double velocityX;

    /**
     * The current velocity downwards. May be negative when the particle moves 
     * upwards.
     */
    private double velocityY;

    /**
     * Constructs a new particle.
     * 
     * @param mass the weight of the new particle.
     */
    public Particle(double mass) {
        this.mass = checkMass(mass);
    }

    /**
     * Copy-constructs a new particle.
     * 
     * @param other the other particle to copy.
     */
    public Particle(Particle other) {
        this.mass      = other.mass;
        this.x         = other.x;
        this.y         = other.y;
        this.velocityX = other.velocityX;
        this.velocityY = other.velocityY;
    }

    public Vector getVelocityVector() {
        return new Vector(velocityX, velocityY);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getMass() {
        return mass;
    }

    public void setX(double x) {
        this.x = checkX(x);
    }

    public void setY(double y) {
        this.y = checkY(y);
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = checkVelocityX(velocityX);
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = checkVelocityY(velocityY);
    }

    public double getSpeed() {
        double vxSquared = velocityX * velocityX;
        double vySquared = velocityY * velocityY;
        return Math.sqrt(vxSquared + vySquared);
    }

    public double getDistance(Particle other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getKineticEnergy() {
        double speed = getSpeed();
        return 0.5 * mass * speed * speed;
    }

    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + ", velocityX=" + velocityX + 
               ", velocityY=" + velocityY + "]";
    }

    private double checkMass(double mass) {
        checkNonNaN(mass, "The particle mass is NaN.");
        checkNonNegative(mass, "The particle mass is non-positive.");
        checkNonInfinite(mass, "The particle mass is infinite.");
        return mass;
    }

    private double checkCoordinate(double coordinate,
                                   String errorMessageNaN,
                                   String errorMessageInfinite) {
        checkNonNaN(coordinate, errorMessageNaN);
        checkNonInfinite(coordinate, errorMessageInfinite);
        return coordinate;
    }

    private double checkX(double x) {
        checkCoordinate(x,
                        "The x-coordinate is NaN.", 
                        "The x-coordinate is infinite.");
        return x;
    }

    private double checkY(double y) {
        checkCoordinate(y,
                        "The y-coordinate is NaN.", 
                        "The y-coordinate is infinite.");
        return y;
    }

    private double checkVelocityX(double velocityX) {
        checkCoordinate(velocityX,
                        "The x-velocity is NaN.",
                        "The x-velocity is infinite.");
        return velocityX;
    }

    private double checkVelocityY(double velocityY) {
        checkCoordinate(velocityY,
                        "The y-velocity is NaN.", 
                        "The y-velocity is infinite.");
        return velocityY;
    }
}
