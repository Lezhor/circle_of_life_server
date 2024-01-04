package de.htw_berlin.application;

import de.htw_berlin.engines.AuthenticationEngine;
import de.htw_berlin.engines.AuthenticationEngineImpl;
import de.htw_berlin.engines.SyncEngine;
import de.htw_berlin.engines.SyncEngineImpl;

/**
 * offers getters for all singleton classes
 */
public final class App {

    public static AuthenticationEngine getAuthentication() {
        return AuthenticationEngineImpl.getInstance();
    }

    public static SyncEngine getSyncEngine() {
        return SyncEngineImpl.getInstance();
    }

}
