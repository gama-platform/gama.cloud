package ummisco.gama.java2d;
/**
 * A utility class for checking parameters.
 *
 * @since 1.0.14
 */
public class ParamChecks {

    /**
     * Throws an <code>IllegalArgumentException</code> if the supplied 
     * <code>param</code> is <code>null</code>.
     *
     * @param param  the parameter to check (<code>null</code> permitted).
     * @param name  the name of the parameter (to use in the exception message
     *     if <code>param</code> is <code>null</code>).
     *
     * @throws IllegalArgumentException  if <code>param</code> is 
     *     <code>null</code>.
     *
     * @since 1.0.14
     */
    public static void nullNotPermitted(Object param, String name) {
        if (param == null) {
            throw new IllegalArgumentException("Null '" + name + "' argument.");
        }
    }

    /**
     * Throws an {@code IllegalArgumentException} if {@code value} is negative.
     * 
     * @param value  the value.
     * @param name  the parameter name (for use in the exception message).
     * 
     * @since 1.0.18
     */
    public static void requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException("Require '" + name + "' (" + value + ") to be non-negative.");
        }
    }
}