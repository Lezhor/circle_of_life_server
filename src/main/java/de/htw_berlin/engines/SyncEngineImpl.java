package de.htw_berlin.engines;

import de.htw_berlin.engines.models.DBLog;

import java.time.LocalDateTime;
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
    public List<DBLog<?>> sync(LocalDateTime lastSync, DBLog<?>... logs) {
        // TODO: 04.01.2024 implement syncing with db
        return new LinkedList<>();
    }
}
