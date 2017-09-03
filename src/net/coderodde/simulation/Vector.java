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
    
    public double dotProduct(Vector other) {
        return x * other.x + y * other.y;
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
    
    private void checkNonNaN(double value, String errorMessage) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    private void checkNonInfinite(double value, String errorMessage) {
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
