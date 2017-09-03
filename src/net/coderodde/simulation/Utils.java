package net.coderodde.simulation;

public final class Utils {

    private Utils() {}

    static void checkNonNaN(double value, String errorMessage) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    static void checkNonNegative(double value, String errorMessage) {
        if (value < 0.0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    static void checkNonInfinite(double value, String errorMessage) {
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
