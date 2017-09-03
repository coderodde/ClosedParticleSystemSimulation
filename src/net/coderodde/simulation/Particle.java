package net.coderodde.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Objects;

/**
 * This class defines a particle in the simulation. The entire weight of a
 * particle is considered to be fully focused in the center of this particle.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class Particle {

    private static final double GRAVITATIONAL_CONSTANT = 10.0;
    
    /**
     * The weight of this particle.
     */
    private final double mass;
    
    /**
     * The radius of the graphical representation of this particle.
     */
    private final int radius;
    
    /**
     * The color of the graphical representation of this particle.
     */
    private final Color color;
    
    /**
     * The current x-coordinate of this particle.
     */
    private double x;
    
    /**
     * The current y-coordinate of this particle.
     */
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
     * @param radius the radius of the new particle.
     * @param color  the color of the new particle.
     */
    public Particle(double mass, int radius, Color color) {
        this.mass = checkMass(mass);
        this.radius = checkRadius(radius);
        this.color = Objects.requireNonNull(color, 
                                           "The particle color is null.");
    }
    
    public Particle(Particle other) {
        this.mass      = other.mass;
        this.radius    = other.radius;
        this.color     = other.color;
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
    
    /**
     * Returns the current speed of this particle.
     * 
     * @return the current speed.
     */
    public double getSpeed() {
        double vxSquared = velocityX * velocityX;
        double vySquared = velocityY * velocityY;
        return Math.sqrt(vxSquared + vySquared);
    }
    
    /**
     * Returns the distance between this particle and {@code other}.
     * 
     * @param other the other particle.
     * @return the distance between two particles.
     */
    public double getDistance(Particle other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Computes the kinetic energy of this particle.
     * 
     * @return the kinetic energy.
     */
    public double getKineticEnergy() {
        double speed = getSpeed();
        return 0.5 * mass * speed * speed;
    }
    
    /**
     * Computes the rejection force between this and {@code other} particles.
     * 
     * @param other the other particle.
     * @return the rejection force.
     */
    public double getRejectionForce(Particle other) {
        double distance = getDistance(other);
        return GRAVITATIONAL_CONSTANT * mass * other.getMass() / 
               (distance * distance);
    }
    
    /**
     * Computes the potential energy between this and {@code other} particle.
     * 
     * @param other the other particle.
     * @return potential energy.
     */
    public double getPotentialEnergy(Particle other) {
        double distance = getDistance(other);
        return GRAVITATIONAL_CONSTANT * mass * other.getMass() / distance;
    }
    
    public void draw(Graphics g) {
        int effectiveX = (int) x;
        int effectiveY = (int) y;
        
        g.setColor(color);
        g.fillOval(effectiveX - radius, 
                   effectiveY - radius,
                   2 * radius,
                   2 * radius);
    }
    
    public String toString() {
        return "[x=" + x + ", y=" + y + ", velocityX=" + velocityX + 
               ", velocityY=" + velocityY + "]";
    }
    
    private double checkMass(double mass) {
        if (Double.isNaN(mass)) {
            throw new IllegalArgumentException("The particle mass is NaN.");
        }
        
        if (mass <= 0.0) {
            throw new IllegalArgumentException(
                    "The particle mass is non-positive.");
        }
        
        if (Double.isInfinite(mass)) {
            throw new IllegalArgumentException(
                    "The particle mass is infinite.");
        }
        
        return mass;
    }
    
    private int checkRadius(int radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException(
                    "The particle radius is non-positive: " + radius);
        }
        
        return radius;
    }
    
    private void checkNotNaN(double d, String errorMessage) {
        if (Double.isNaN(d)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    private void checkNotInfinite(double d, String errorMessage) {
        if (Double.isInfinite(d)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    private void checkNotNaNOrInfinite(double d, 
                                       String errorMessageNaN,
                                       String errorMessageInfinite) {
        checkNotNaN(d, errorMessageNaN);
        checkNotInfinite(d, errorMessageInfinite);
    }
    
    private double checkX(double x) {
        checkNotNaNOrInfinite(x,
                              "The x-coordinate is NaN.", 
                              "The x-coordinate is infinite.");
        return x;
    }
    
    private double checkY(double y) {
        checkNotNaNOrInfinite(y,
                              "The y-coordinate is NaN.", 
                              "The y-coordinate is infinite.");
        return y;
    }
    
    private double checkVelocityX(double velocityX) {
        checkNotNaNOrInfinite(velocityX,
                              "The x-velocity is NaN.",
                              "The x-velocity is infinite.");
        return velocityX;
    }
    
    private double checkVelocityY(double velocityY) {
        checkNotNaNOrInfinite(velocityY,
                              "The y-velocity is NaN.", 
                              "The y-velocity is infinite.");
        return velocityY;
    }
}
