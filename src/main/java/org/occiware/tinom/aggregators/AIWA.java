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

public class AIWA implements Aggregation {

    public static final ParametricFactory FACTORY = new ParametricFactory() {
        @Override
        public AIWA create(double... params) {
            if (params == null || params.length != 1)
                throw new IllegalArgumentException("AIWA factory need one parameter (p)");
            return new AIWA(params[0]);
        }
    };

    public final double p;
    public final double alpha;

    public AIWA(double p) {
        if (0 > p || p > 1) throw new IllegalArgumentException("p must be within [0, 1]");
        this.p = p;
        this.alpha = (1. - p) / p;
    }

    @Override
    public double apply(double... values) {

        if (p <= .5) {
            double s = 0.;
            double d = 0.;
            for (int i = 0; i < values.length; i++) {
                s += Math.pow(values[i], alpha);
                d += 1.0;
            }
            return Math.pow(s / d, 1. / alpha);
        }
        else {
            double s = 0.;
            double d = 0.;
            for (int i = 0; i < values.length; i++) {
                s += Math.pow(1. - values[i], 1./alpha);
                d += 1.0;
            }
            return 1. - Math.pow(s / d, alpha);
        }
    }
}
