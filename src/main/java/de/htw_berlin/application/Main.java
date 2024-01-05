package de.htw_berlin.application;

import de.htw_berlin.communication.Server;
import de.htw_berlin.database.models.User;
import de.htw_berlin.logging.Log;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Main-class. This is where to launch the program
 */
public class Main {
    private static final String TAG = Main.class.getSimpleName();

    /**
     * Main-method
     * @param args empty
     */
    public static void main(String[] args) {
        Log.logToConsole(true);
        Log.d(TAG, "Starting Application...");
        Server server = new Server();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutDown));

        User user = new User(UUID.randomUUID(), "alice_connor", "kdjabskk", LocalDateTime.now());

        Log.d(TAG, "Exists: " + App.getDatabaseController().exists(user));
        App.getDatabaseController().insert(user);
        Log.d(TAG, "Exists: " + App.getDatabaseController().exists(user));

        User user2 = App.getDatabaseController().getById(user.getId(), User.class);

        Log.d(TAG, "Equals: " + user.equalsAllParams(user2));

        App.getDatabaseController().delete(user);

    }

}
