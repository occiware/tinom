package org.occiware.tinom.aggregators;

/**
 * @author Mehdi Ahmed-Nacer
 */
public interface ParametricFactory<T> {
    public T create(double... params);
}
