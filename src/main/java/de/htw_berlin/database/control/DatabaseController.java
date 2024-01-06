package de.htw_berlin.database.control;

import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Methods for changing data
 */
public interface DatabaseController {

    <E extends Entity> boolean executeLog(DBLog<E> log);

    <E extends Entity> boolean insert(E entity);

    /**
     * Inserts multiple entities into database
     * @param entities entities
     * @return true if all entities are inserted successfully
     * @see #insert(Entity)
     */
    default boolean insertAll(Entity... entities) {
        boolean successful = true;
        for (Entity entity : entities) {
            if (!insert(entity)) {
                successful = false;
            }
        }
        return successful;
    }

    <E extends Entity> boolean update(E entity);

    /**
     * Updates multiple entities in database
     * @param entities entities
     * @return true if all entities are updated successfully
     * @see #update(Entity)
     */
    default boolean updateAll(Entity... entities) {
        boolean successful = true;
        for (Entity entity : entities) {
            if (!update(entity)) {
                successful = false;
            }
        }
        return successful;
    }

    /**
     * Deletes entity from database
     * @param entity entity
     * @return true if no error occurs. Note that if entity didn't exist in database the method will still return true
     * @param <E> type of the entity
     */
    <E extends Entity> boolean delete(E entity);

    /**
     * Deletes multiple entities from database
     * @param entities entities
     * @return true if all entities are deleted successfully.
     * @see #delete(Entity)
     */
    default boolean deleteAll(Entity... entities) {
        boolean successful = true;
        for (Entity entity : entities) {
            if (!delete(entity)) {
                successful = false;
            }
        }
        return successful;
    }

    <E extends Entity> E getById(UUID id, Class<E> entityClass);
    <E extends Entity> boolean exists(E entity);

    /**
     * Checks exists on multiple entities in database
     * @param entities entities
     * @return true if all passed entities exist in database
     * @see #exists(Entity)
     */
    default boolean existAll(Entity... entities) {
        boolean successful = true;
        for (Entity entity : entities) {
            if (!exists(entity)) {
                successful = false;
            }
        }
        return successful;
    }


    boolean clearUser(User user) throws OperationNotSupportedException;

    /**
     * Searches for user with given username in database
     * @param username username
     * @return user if found oro null if not found
     */
    User getUserByUsername(String username);


    // Logs

    void insertLog(DBLog<?> log);

    /**
     * Inserts multiple logs into database
     * @param logs logs
     */
    default void insertAll(DBLog<?>... logs) {
        Arrays.stream(logs).forEach(this::insertLog);
    }
    void updateLog(DBLog<?> log);
    void deleteLog(DBLog<?> log);


    List<DBLog<?>> getLogsBetweenTimestamps(User client, LocalDateTime timestamp1, LocalDateTime timestamp2);
}
