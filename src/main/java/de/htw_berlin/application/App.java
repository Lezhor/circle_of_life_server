package de.htw_berlin.application;

import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.control.DatabaseControllerImpl;
import de.htw_berlin.engines.AuthenticationEngine;
import de.htw_berlin.engines.AuthenticationEngineImpl;
import de.htw_berlin.engines.SyncEngine;
import de.htw_berlin.engines.SyncEngineImpl;

import java.time.ZoneId;

/**
 * offers getters for all singleton classes
 */
public final class App {

    /**
     * ZoneId of the timezone of the server.
     * @implNote Does not have to be the exact timezone of the server location,
     * everything will work if server and client have this constant configured the same
     */
    public static final ZoneId SERVER_TIMEZONE = ZoneId.of("Europe/Berlin");

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
