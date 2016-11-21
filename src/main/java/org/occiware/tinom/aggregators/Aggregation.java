package org.occiware.tinom.aggregators;

import java.io.Serializable;

public interface Aggregation extends Serializable {
    double apply(double... values);
}
