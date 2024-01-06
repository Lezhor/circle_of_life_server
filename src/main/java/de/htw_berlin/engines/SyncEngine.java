package de.htw_berlin.engines;

import de.htw_berlin.communication.pdus.sync.SendLogsPDU;
import de.htw_berlin.database.models.User;

/**
 * Offers method for synchronizing.
 */
public interface SyncEngine {

    /**
     * Synchronizing takes in an array of logs with changes to be applied to database,
     * as well as lastSyncDate and returns a list of logs which the client has to execute in order to update its own db to the newest state
     * @param clientLogs the incoming pdu whith last sync date as well as the logs
     * @return SendLogsPDU with all instructions for the client as well as a new lastSyncDate
     */
    SendLogsPDU sync(User client, SendLogsPDU clientLogs);

}
