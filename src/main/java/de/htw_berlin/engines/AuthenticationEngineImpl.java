package de.htw_berlin.engines;

import de.htw_berlin.application.App;
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
        User actualUser = App.getDatabaseController().getUserByUsername(user.getUsername());
        if (actualUser == null) {
            return false;
        }
        return actualUser.getPassword().equals(user.getPassword());
    }

    @Override
    public User getAuthenticatedUser(String username, String password) {
        User user = App.getDatabaseController().getUserByUsername(username);
        if (user == null) {
            return null;
        }
        return (user.getPassword().equals(password)) ? user : null;
    }
}
