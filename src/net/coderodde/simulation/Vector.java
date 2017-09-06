package net.coderodde.simulation;

import static net.coderodde.simulation.Utils.checkNonInfinite;
import static net.coderodde.simulation.Utils.checkNonNaN;

/**
 * This class implements a two-dimensional vector.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2017)
 */
public final class Vector {

    /**
     * The x-component of this vector.
     */
    private final double x;

    /**
     * The y-component of this vector.
     */
    private final double y;

    public Vector(double x, double y) {
        this.x = checkX(x);
        this.y = checkY(y);
    }

    public Vector() {
        this(0.0, 0.0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector multiply(double factor) {
        return new Vector(x * factor, y * factor);
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ")";
    }

    private double checkX(double x) {
        return check(x,
                     "The x-component is NaN.",
                     "The x-component is infinite.");
    }

    private double checkY(double y) {
        return check(y,
                     "The y-component is NaN.",
                     "The y-component is infinite.");
    }

    private double check(double value,
                         String errorMessageNaN,
                         String errorMessageInfinite) {
        checkNonNaN(value, errorMessageNaN);
        checkNonInfinite(value, errorMessageInfinite);
        return value;
    }
}
