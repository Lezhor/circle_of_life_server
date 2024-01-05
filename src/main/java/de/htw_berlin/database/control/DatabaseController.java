package de.htw_berlin.database.control;

import de.htw_berlin.database.control.daos.BaseDao;
import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;

import java.util.UUID;

/**
 * Methods for changing data
 */
public interface DatabaseController {

    <E extends Entity> void executeLog(DBLog<E> log);

    <E extends Entity> void insert(E entity);

    <E extends Entity> void update(E entity);
    <E extends Entity> void delete(E entity);
    <E extends Entity> E getById(UUID id, Class<E> entityClass);
    <E extends Entity> boolean exists(E entity);

    /**
     * Searches for user with given username in database
     * @param username username
     * @return user if found oro null if not found
     */
    User getUserByUsername(String username);


    // Logs

    void insertLog(DBLog<?> log);
    void updateLog(DBLog<?> log);
    void deleteLog(DBLog<?> log);



}
