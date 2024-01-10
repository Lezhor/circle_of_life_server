package de.htw_berlin.engines;

import de.htw_berlin.application.App;
import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Override
    public User signUp(String username, String password) {
        DatabaseController db = App.getDatabaseController();
        if (db.getUserByUsername(username) != null) {
            // username taken
            return null;
        } else {
            User user;
            try {
                user = new User(UUID.randomUUID(), username, password, LocalDateTime.now(App.SERVER_TIMEZONE));
            } catch (Exception e) {
                return null;
            }
            if (db.insert(user)) {
                return user;
            } else {
                return null;
            }
        }
    }
}
