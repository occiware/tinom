/**
 * Copyright 2016 Institut mines telecom, Telecom SudParis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.occiware.tinom.aggregators;

import java.util.Arrays;

/**
 * @author Mehdi Ahmed-Nacer - Telecom SudParis
 */

public class OrderedWeightedAveraging implements Aggregation {

    public static final ParametricFactory FACTORY = new ParametricFactory() {
        @Override
        public OrderedWeightedAveraging create(double... params) {
            return new OrderedWeightedAveraging(params);
        }
    };

    public static final ParametricFactory MEOWA = new ParametricFactory() {
        @Override
        public Aggregation create(double... params) {
            if (params == null || params.length < 1)
                throw new IllegalArgumentException("MEOWA factory requires one parameter (rho)");
            final double rho = params[0];
            // aggr
            return new Aggregation() {
                private OrderedWeightedAveraging owa;
                @Override
                public double apply(double... values) {
                    if (owa == null)
                        owa = owafactory(values.length, rho);
                    return owa.apply(values);
                }
            };
        }
    };


    public static OrderedWeightedAveraging owafactory(int n, double rho) {

        if (rho < 0.0 || rho > 1.0)
            throw new IllegalArgumentException("andness (rho) must be within [0, 1]");

        if (rho == 1.0) { // handle border-case, rho = 1.0
            double[] w = new double[n];
            Arrays.fill(w, 0.0);
            w[n-1] = 1.0;
            return new OrderedWeightedAveraging(w);
        }


        // calculate the two roots for rho
        double t_m = (-(rho - 0.5) - Math.sqrt(((rho - 0.5) * (rho - 0.5)) - (4 * (rho - 1) * rho))) / (2 * (rho - 1));
        double t_p = (-(rho - 0.5) + Math.sqrt(((rho - 0.5) * (rho - 0.5)) - (4 * (rho - 1) * rho))) / (2 * (rho - 1));
        double t = Math.max(t_m, t_p);

        // System.out.println("t = " + t);

        double[] w = new double[n];
        double s = 0.0;
        for (int i = 0; i < n; i++) {
            w[i] = Math.pow(t, i);
            s += w[i];
        }
        for (int i = 0; i < n; i++) {
            w[i] /= s;
        }

        return new OrderedWeightedAveraging(w);
    }


    public final double[] weights;

    public OrderedWeightedAveraging(double... weights) {
        this.weights = weights;
        double sum = 0.0;
        for (double w : weights)
            sum += w;
        double diff = Math.abs(1.0 - sum);
        if (diff > 0.0001)
            throw new IllegalArgumentException(String.format("sum(weights) must be 1.0 (was: %f, %.5f)", sum, diff));
    }

    @Override
    public double apply(double... values) {
        // we need same lenghts
        int min = Math.min(values.length, weights.length);
        // sort
        Arrays.sort(values);
        OrderedWeightedAveraging.reverse(values);
        //
        double s = 0.0;
        for (int i = 0; i < min; i++)
            s += values[i] * weights[i];

        return s;
    }

    /**
     * Calculate the orness of this OWA operator.
     *
     * To get the andness use it's definition: <code>double andness = 1.0 - orness();</code>
     *
     * @return the orness [0, 1]
     */
    public double orness() {
        double n = weights.length;
        double s = 0.0;
        for (int i = 0; i < weights.length; i++)
            s += (n - (i+1)) * weights[i];
        return s / (n - 1.0);
    }

    /**
     * The dual of orness. See {@link #orness()}
     * @return
     */
    public double andness() {
        return 1.0 - orness();
    }

    /**
     * Calculate the relative dispersion (entropy) of the OWA operator.
     *
     * @return the dispersion [0, ln n]
     */
    public double dispersion() {
        double n = weights.length;
        double s = 0.0;
        for (double w : weights)
            if (w > 0.0)
                s += w * Math.log(1.0 / w);
        return s / Math.log(n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weights.length; i++)
            sb.append(" " + weights[i]);
        return "OWA{" +
                "weights=" + sb.substring(1).toString() +
                '}';
    }

    /**
     * Reverse an array.
     *
     * @param array the array
     */
    public static void reverse(double[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }



}
