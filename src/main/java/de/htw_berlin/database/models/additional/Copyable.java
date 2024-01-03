package de.htw_berlin.database.models.additional;

/**
 * An interface which offers the clone method.
 * @param <E> type that is returned. should be same class as the class implementing this interface.
 */
public interface Copyable<E> {
    E copy();
}
