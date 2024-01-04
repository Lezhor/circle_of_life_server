package de.htw_berlin.application;

import de.htw_berlin.engines.AuthenticationEngine;
import de.htw_berlin.engines.AuthenticationEngineImpl;

/**
 * offers getters for all singleton classes
 */
public final class App {

    public static AuthenticationEngine getAuthentication() {
        return AuthenticationEngineImpl.getInstance();
    }

}
