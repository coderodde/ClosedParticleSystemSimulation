package net.coderodde.simulation;

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
    
    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }
    
    public double dotProduct(Vector other) {
        return x * other.x + y * other.y;
    }
    
    private double checkX(double x) {
        return check(x,
                     "The x-component is NaN.",
                     "The x-component is negative: " + x,
                     "The x-component is infinite.");
    }
    
    private double checkY(double y) {
        return check(y,
                     "The y-component is NaN.",
                     "The y-component is negative: " + x,
                     "The y-component is infinite.");
    }
    
    private double check(double value,
                         String errorMessageNaN,
                         String errorMessageNegative,
                         String errorMessageInfinite) {
        checkNonNaN(value, errorMessageNaN);
        checkNonNegative(value, errorMessageNegative);
        checkNonInfinite(value, errorMessageInfinite);
        return value;
    }
    
    private void checkNonNaN(double value, String errorMessage) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    private void checkNonNegative(double value, String errorMessage) {
        if (value < 0.0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    private void checkNonInfinite(double value, String errorMessage) {
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
