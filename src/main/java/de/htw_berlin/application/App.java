package de.htw_berlin.application;

import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.control.DatabaseControllerImpl;
import de.htw_berlin.engines.AuthenticationEngine;
import de.htw_berlin.engines.AuthenticationEngineImpl;
import de.htw_berlin.engines.SyncEngine;
import de.htw_berlin.engines.SyncEngineImpl;

/**
 * offers getters for all singleton classes
 */
public final class App {

    public static AuthenticationEngine getAuthenticationEngine() {
        return AuthenticationEngineImpl.getInstance();
    }

    public static SyncEngine getSyncEngine() {
        return SyncEngineImpl.getInstance();
    }

    public static DatabaseController getDatabaseController() {
        return DatabaseControllerImpl.getInstance();
    }

}
