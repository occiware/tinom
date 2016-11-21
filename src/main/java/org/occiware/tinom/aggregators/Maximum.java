
package org.occiware.tinom.aggregators;

/**
 * Maximum (max) aggregation operator
 *
 * @author Mehdi Ahmed-Nacer
 */
public class Maximum implements Aggregation {

    public static final Maximum INSTANCE = new Maximum();

    @Override
    public double apply(double... values) {
        return minmax(values)[1];
    }


    public static double[] minmax(double[] values) {
        if (values == null || values.length == 0)
            return new double[] { Double.NaN, Double.NaN };
        double min = values[0], max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min)
                min = values[i];
            if (values[i] > max)
                max = values[i];
        }
        return new double[]{ min, max };
    }
}
