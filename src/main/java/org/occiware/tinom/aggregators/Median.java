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

public class Median implements Aggregation {

    public static Median INSTANCE = new Median();

    @Override
    public double apply(double... values) {
        if (values == null || values.length == 0)
            return Double.NaN;
        Arrays.sort(values);
        int middle = values.length / 2;
        if (values.length % 2 == 0)
            return (values[middle] + values[middle - 1]) / 2;
        else
            return values[middle];
    }

    public String toString() {
        return "h_{median}";
    }
}
