package de.htw_berlin.engines;

import de.htw_berlin.database.models.User;

/**
 * Is for authenticating users.
 * @see AuthenticationEngineImpl
 */
public interface AuthenticationEngine {

    /**
     * Checks whether the passed user is authenticated.
     * @param user user
     * @return true if user is in database (with passed name and password)
     */
    boolean isAuthenticated(User user);

    /**
     * Searches for user in database with given username and checks if the given password matches
     * @param username username
     * @param password password
     * @return user if found in db or null if not found or if password doesn't match
     */
    User getAuthenticatedUser(String username, String password);

    /**
     * Creates new user and inserts into database, if username is not taken, ans username and password are valid
     * @param username username
     * @param password password
     * @return created user or null if didn't work
     */
    User signUp(String username, String password);

}
