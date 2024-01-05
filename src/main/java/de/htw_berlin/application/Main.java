package de.htw_berlin.application;

import de.htw_berlin.communication.Server;
import de.htw_berlin.logging.Log;


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

    }

}
