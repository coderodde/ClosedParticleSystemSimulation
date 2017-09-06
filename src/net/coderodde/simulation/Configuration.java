package net.coderodde.simulation;

/**
 * Holds the physics related constants.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 6, 2017)
 */
public final class Configuration {

    /**
     * Defines the drawing scale. A distance of one unit length corresponds to 
     * the length of 100 pixels.
     */
    public static final int PIXELS_PER_UNIT_LENGTH = 10;

    /**
     * The rejection force constant.
     */
    public static final double FORCE_CONSTANT = 1000.0;

    /**
     * The minimum particle mass.
     */
    public static final double MINIMUM_PARTICLE_MASS = 10.0;

    /**
     * The maximum particle mass.
     */
    public static final double MAXIMUM_PARTICLE_MASS = 30.0;
    
    /**
     * The number of milliseconds spent between two consecutive time quants.
     */
    public static final int SLEEP_TIME = 10;

    /**
     * The time step.
     */
    public static final double TIME_STEP = 0.01;
    
    /**
     * The maximum initial velocity horizontally and/or vertically.
     */
    public static final double MAX_INITIAL_VELOCITY = 40.0;
    
    /**
     * The default number of particles in the simulation.
     */
    public static final int DEFAULT_NUMBER_OF_PARTICLES = 3;
}
