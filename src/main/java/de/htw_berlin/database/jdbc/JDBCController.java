package de.htw_berlin.database.jdbc;

import de.htw_berlin.logging.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCController {
    private static final String TAG = JDBCController.class.getSimpleName();

    public static void executeInDB(SQLQuery task) {
        executeInDB(con -> {
            task.execute(con);
            return null;
        });
    }

    public static <T> T executeInDB(SQLQueryWithResult<T> task) {
        Connection connection = null;
        T result = null;

        try {

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/circle_of_life_db", "postgres", "root");
        } catch (SQLException | ClassNotFoundException e) {
            Log.w(TAG, "Connection failed!!!", e);
        }
        try {
            if (connection != null) {
                result = task.execute(connection);
            }
        } catch (SQLException e) {
            Log.w(TAG, "Executing query failed", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return result;
    }

}
