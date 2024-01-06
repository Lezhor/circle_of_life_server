package de.htw_berlin.database.control.daos;

import de.htw_berlin.database.models.Entity;
import de.htw_berlin.engines.models.DBLog;

import java.util.UUID;

/**
 * Every Dao (Data Access Object) implements the BaseDao. it offers methods for inserting, updating deleting, checking for existence.
 * Also, it has template methods for the basic operations which first check if the entity exists before doing it.
 * @param <T> type of the entity e.g. {@link de.htw_berlin.database.models.User}, {@link de.htw_berlin.database.models.Category}, ...
 */
public interface BaseDao<T extends Entity> {

    /**
     * Inserts entity into database
     * @param entity entity
     * @return true if inserted successfully
     */
    boolean insertWithoutCheckingExistence(T entity);

    /**
     * Inserts entity into database
     * @param entity entity
     * @return true if inserted successfully
     * @see #insertWithoutCheckingExistence(Entity)
     */
    default boolean insert(T entity) {
        return insertWithoutCheckingExistence(entity);
    }

    /**
     * Updates entity in database
     * @param entity entity
     * @return true if updated successfully
     */
    boolean updateWithoutCheckingExistence(T entity);

    /**
     * Returns true if entity exists in database and was successfully updated
     * @param entity entity
     * @return true if updated. false if something went wrong or entity is not in database
     */
    default boolean update(T entity) {
        if (exists(entity)) {
            return updateWithoutCheckingExistence(entity);
        } else {
            return false;
        }
    }

    /**
     * Deletes entity from database
     * @param entity entity
     * @return true if deleted successfully
     */
    boolean deleteWithoutCheckingExistence(T entity);

    /**
     * Deletes entity from database, if there
     * @param entity entity
     * @return true if entity deleted. false if something went wrong or entity was not in database.
     */
    default boolean delete(T entity) {
        if (exists(entity)) {
            return deleteWithoutCheckingExistence(entity);
        } else {
            return false;
        }
    }

    /**
     * Searches through database for entity with given id
     * @param id id
     * @return entity or null if nothing found
     */
    T getById(UUID id);

    /**
     * Checks if passed entity exists in database. Makes use of {@link #getById(UUID)} method.
     * @param entity entity to be checked
     * @return true if entity exists in database
     * @see #getById(UUID)
     */
    default boolean exists(T entity) {
        return getById(entity.getId()) != null;
    }

    /**
     * Executes the content of a {@link DBLog}
     * @param log log to be executed
     * @return true if log was executed successfully
     */
    default boolean executeLog(DBLog<T> log) {
        return switch (log.getChangeMode()) {
            case INSERT -> insert(log.getChangedObject());
            case UPDATE -> update(log.getChangedObject());
            case DELETE -> delete(log.getChangedObject());
        };
    }

}
