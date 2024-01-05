package de.htw_berlin.database.control.daos;

import de.htw_berlin.database.models.Entity;
import de.htw_berlin.engines.models.DBLog;

import java.util.UUID;

public interface BaseDao<T extends Entity> {

    void insert(T entity);

    void update(T entity);

    void delete(T entity);

    /**
     * Searches through database for entity with given id
     * @param id id
     * @return entity or null if nothing found
     */
    T getById(UUID id);

    default boolean exists(T entity) {
        return getById(entity.getId()) != null;
    }

    /**
     * Executes the content of a {@link DBLog}
     * @param log log to be executed
     */
    default void executeLog(DBLog<T> log) {
        switch (log.getChangeMode()) {
            case INSERT -> insert(log.getChangedObject());
            case UPDATE -> update(log.getChangedObject());
            case DELETE -> delete(log.getChangedObject());
        }
    }

}
