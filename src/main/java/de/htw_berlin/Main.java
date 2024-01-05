package de.htw_berlin;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.Server;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.User;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;
import de.htw_berlin.logging.Log;

import java.sql.ResultSet;
import java.sql.Statement;
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
        UUID uuid = UUIDConverter.uuidFromString("3ad68867-341f-4e7b-854c-1b76059e67fc");
        Log.d(TAG, "UUID: " + UUIDConverter.uuidToString(uuid));
        Log.d(TAG, "Starting Application...");
        Server server = new Server();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutDown));

        User user = new User(uuid, "username", "password", LocalDateTime.now());
        //AppDatabase.getUserDao().insert(user);

        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE \"userID\" = '" + UUIDConverter.uuidToString(uuid) + "' LIMIT 1");
            if (rs.next()) {
                user.setId(UUIDConverter.uuidFromString(rs.getString("userID")));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setTimeOfCreation(LocalDateTimeConverter.localDateTimeFromString(rs.getString("creation_time")));
            } else {
                Log.d(TAG, "No User found");
            }
        });

        user.setTimeOfCreation(LocalDateTime.now());
        user.setUsername("john_doe");

        App.getDatabaseController().updateUser(user);
        //AppDatabase.getUserDao().insert(new User(UUID.randomUUID(), "jane_doe", "another_password", LocalDateTime.now()));

        User user2 = App.getDatabaseController().getUserByUsername("jane_doe");
        Log.d(TAG, "Retrieved user: " + user2);

        //AppDatabase.getUserDao().delete(user);
    }

}
