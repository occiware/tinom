
package org.occiware.tinom.aggregators;

/**
 * Aggregation operator using arithmetic mean.
 *
 * @author Mehdi Ahmed-Nacer
 */
public class ArithmeticMean implements Aggregation {

    public static ArithmeticMean INSTANCE = new ArithmeticMean();

    @Override
    public double apply(double... values) {
        if (values == null || values.length == 0)
            return Double.NaN;
        double s = 0.0;
        for (int i = 0; i < values.length; i++)
            s += values[i];
        return s / values.length;
    }

    @Override
    public String toString() {
        return "h_{arithmetic}";
    }
}
