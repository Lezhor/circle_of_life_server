package de.htw_berlin.database.jdbc;

import de.htw_berlin.logging.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCController {

    public static final String JDBC_IP = "localhost";
    public static final String JDBC_DATABASE = "circle_of_life_db";
    public static final String JDBC_USERNAME = "postgres";
    public static final String JDBC_PASSWORD = "root";

    public static final String JDBC_CONNECTION_STRING = "jdbc:postgresql://" + JDBC_IP + ":5432/" + JDBC_DATABASE;


    private static final String TAG = JDBCController.class.getSimpleName();

    public static <T> T executeInDB(SQLQueryWithResult<T> task) {
        Connection connection = null;
        T result = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "JDBC-Driver not found!", e);
            return null;
        }
        try {
            connection = DriverManager.getConnection(JDBC_CONNECTION_STRING, JDBC_USERNAME, JDBC_PASSWORD);
        } catch (SQLException e) {
            Log.w(TAG, "Connection failed!!!", e);
        }
        if (connection != null) {
            result = task.execute(connection);
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
        return result;
    }

}
