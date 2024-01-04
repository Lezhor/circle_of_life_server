package de.htw_berlin.engines;

import de.htw_berlin.engines.models.DBLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Offers method for synchronizing.
 */
public interface SyncEngine {

    /**
     * Synchronizing takes in an array of logs with changes to be applied to database,
     * as well as lastSyncDate and returns a list of logs which the client has to execute in order to update its own db to the newest state
     * @param lastSync lastSyncDate
     * @param logs logArray
     * @return list of logs the client needs to execute
     */
    List<DBLog<?>> sync(LocalDateTime lastSync, DBLog<?>... logs);

}
