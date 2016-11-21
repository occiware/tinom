
package org.occiware.tinom.aggregators;

/**
 * Minimum (min) aggregation operator.
 *
 * @author Mehdi Ahmed-Nacer
 */
public class Minimum implements Aggregation {

    public static final Minimum INSTANCE = new Minimum();

    @Override
    public double apply(double... values) {
        return Maximum.minmax(values)[0];
    }

}
