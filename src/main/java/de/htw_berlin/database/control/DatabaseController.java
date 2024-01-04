package de.htw_berlin.database.control;

import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;

/**
 * Methods for changes data
 */
public interface DatabaseController {

    <E extends Entity> void executeLog(DBLog<E> log);

}
