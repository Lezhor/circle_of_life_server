package de.htw_berlin.engines;

import de.htw_berlin.database.models.User;

/**
 * Implementation of interface {@link AuthenticationEngine}.
 */
public class AuthenticationEngineImpl implements AuthenticationEngine {

    private static volatile AuthenticationEngineImpl instance;

    /**
     * Singleton instance getter
     * @return only existing instance of this class
     */
    public static AuthenticationEngine getInstance() {
        if (instance == null) {
            synchronized (AuthenticationEngineImpl.class) {
                if (instance == null) {
                    instance = new AuthenticationEngineImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton pattern
     */
    private AuthenticationEngineImpl() {
    }

    @Override
    public boolean isAuthenticated(User user) {
        // TODO: 04.01.2024 Check for Authentication in database
        return true;
    }

    @Override
    public User getAuthenticatedUser(String username, String password) {
        // TODO: 04.01.2024 Search for user in database
        return null;
    }
}
