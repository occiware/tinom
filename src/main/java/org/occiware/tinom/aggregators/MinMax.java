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

/**
 * @author Mehdi Ahmed-Nacer - Telecom SudParis
 */

public class MinMax implements Aggregation {

    public static MinMax MIDRANGE = new MinMax(0.5);

    public static ParametricFactory FACTORY = new ParametricFactory() {
        @Override
        public MinMax create(double... params) {
            if (params == null || params.length != 1)
                throw new IllegalArgumentException("minmax expects one parameter (p)");
            return new MinMax(params[0]);
        }
    };

    private double p;

    public MinMax(double p) {
        if (p < 0 || p > 1)
            throw new IllegalArgumentException("p must be in [0, 1]");
        this.p = p;
    }

    @Override
    public double apply(double... values) {
        double[] minmax = Maximum.minmax(values);
        return (1.-p) * minmax[0] + p * minmax[1];
    }
}
