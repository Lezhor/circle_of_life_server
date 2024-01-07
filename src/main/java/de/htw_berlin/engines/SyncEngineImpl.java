package de.htw_berlin.engines;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.pdus.sync.SendLogsPDU;
import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.models.User;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.engines.models.DBLogQueue;

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

        DatabaseController db = App.getDatabaseController();

        LocalDateTime newLastSyncDate = LocalDateTime.now();
        LocalDateTime insertTimestamp = newLastSyncDate.minusNanos(1);

        List<DBLog<?>> clientLogs = new LinkedList<>(Arrays.asList(clientLogsPDU.getLogs()));
        List<DBLog<?>> serverLogs = db.getLogsBetweenTimestamps(client, clientLogsPDU.getLastSyncDate(), newLastSyncDate);

        DBLogQueue queue = new DBLogQueue(serverLogs, clientLogs);

        List<DBLog<?>> instructions = new LinkedList<>();
        List<DBLog<?>> clientLogsExecuted = new LinkedList<>();

        while (queue.hasNext()) {
            boolean isClient = queue.isNextClient();
            DBLog<?> log = queue.poll();
            boolean validLog = true;
            if (isClient) {
                if (db.executeLog(log)) {
                    db.insertLog(log, insertTimestamp);
                    clientLogsExecuted.add(log);
                } else {
                    validLog = false;
                }
            } else {
                int backup = clientLogsExecuted.size();
                removeOverriddenLogs(clientLogsExecuted, log);
                if (clientLogsExecuted.size() < backup) {
                    db.executeLog(log);
                }
            }
            if (validLog) {
                removeOverriddenLogs(instructions, log);
                if (!isClient) {
                    instructions.add(log);
                }
            }
        }

        return new SendLogsPDU(newLastSyncDate, instructions.toArray(DBLog[]::new));
    }

    /**
     * Removes logs from passed logs-list which would become redundant, if <code>overridingLog</code> were added to the list.<br>
     * This could happen in multiple different circumstances. Checking if a log would override another one happens in the {@link #overrides(DBLog, DBLog)} method.
     * @param logs log-list
     * @param overridingLog the log to be checked for. everything that is overridden by this log gets removed
     * @see #overrides(DBLog, DBLog)
     */
    private void removeOverriddenLogs(List<DBLog<?>> logs, DBLog<?> overridingLog) {
        List<DBLog<?>> toBeRemoved = new LinkedList<>();
        for (DBLog<?> log : logs) {
            if (overrides(log, overridingLog)) {
                toBeRemoved.add(log);
            }
        }
        logs.removeAll(toBeRemoved);
    }

    /**
     * Checks if oldLog is overridden by newLog.<br>
     * If oldLog is overridden by newLog, oldLog can be removed without changing the database.<br><br>
     * For this to be true the first criteria is that both log's changedItem is the same.<br>
     * If fist criteria is met the result (whether oldLog is overridden by the new one or not) is determined as follows:<br>
     * <pre>
     *     1) true if oldLog updates and newLog updates
     *     2) true if newLog is delete (no matter what old one was)
     *     3) false if newLog is insert
     *     4) false in every other case
     * </pre>
     * @implNote It is assumed and NOT checked that oldLog comes before newLog
     * @param oldLog oldLog
     * @param newLog newLog
     * @return whether the oldLog is overridden by the new one or not.
     */
    private boolean overrides(DBLog<?> oldLog, DBLog<?> newLog) {
        if (oldLog.getChangedObject().equals(newLog.getChangedObject())) {
            if (newLog.getChangeMode() == DBLog.ChangeMode.DELETE) {
                return true;
            }
            return oldLog.getChangeMode() == DBLog.ChangeMode.UPDATE && newLog.getChangeMode() == DBLog.ChangeMode.UPDATE;
        }
        return false;
    }
}
