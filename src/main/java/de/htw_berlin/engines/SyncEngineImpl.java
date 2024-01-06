package de.htw_berlin.engines;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.pdus.sync.SendLogsPDU;
import de.htw_berlin.database.models.User;
import de.htw_berlin.engines.models.DBLog;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements the {@link SyncEngine}
 */
public class SyncEngineImpl implements SyncEngine {

    private static volatile SyncEngineImpl instance;

    public static SyncEngineImpl getInstance() {
        if (instance == null) {
            synchronized (SyncEngineImpl.class) {
                if (instance == null) {
                    instance = new SyncEngineImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton
     */
    private SyncEngineImpl() {
    }

    @Override
    public SendLogsPDU sync(User client, SendLogsPDU clientLogsPDU) {
        // TODO: 04.01.2024 implement syncing with db

        LocalDateTime newLastSyncDate = LocalDateTime.now();

        List<DBLog<?>> clientLogs = new LinkedList<>(Arrays.asList(clientLogsPDU.getLogs()));
        List<DBLog<?>> serverLogs = App.getDatabaseController().getLogsBetweenTimestamps(client, clientLogsPDU.getLastSyncDate(), newLastSyncDate);

        return new SendLogsPDU(clientLogsPDU.getLastSyncDate());
    }
}
