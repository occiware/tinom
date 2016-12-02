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
