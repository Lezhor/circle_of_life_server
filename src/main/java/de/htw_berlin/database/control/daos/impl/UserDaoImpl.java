package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.UserDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.User;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;
import de.htw_berlin.logging.Log;

import java.sql.ResultSet;
import java.sql.Statement;

public class UserDaoImpl implements UserDao {
    private static final String TAG = "UserDao";

    private static volatile UserDaoImpl instance;

    /**
     * getter for singleton pattern
     * @return only existing instance of this class
     */
    public static UserDao getInstance() {
        if (instance == null) {
            synchronized (UserDaoImpl.class) {
                if (instance == null) {
                    instance = new UserDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton pattern
     */
    private UserDaoImpl() {
    }

    @Override
    public void insert(User user) {
        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            statement.execute("INSERT INTO users (\"userID\", \"username\", \"password\", \"creation_time\") VALUES (" +
                    "'" + UUIDConverter.uuidToString(user.getUserID()) + "', " +
                    "'" + user.getUsername() + "', " +
                    "'" + user.getPassword() + "', " +
                    "'" + LocalDateTimeConverter.localDateTimeToString(user.getTimeOfCreation()) + "')");
            Log.d(TAG, "Inserted user: " + user);
        });
    }

    @Override
    public void update(User user) {
        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            statement.execute("UPDATE users SET " +
                    "username = '" + user.getUsername() + "', " +
                    "password = '" + user.getPassword() + "', " +
                    "creation_time = '" + LocalDateTimeConverter.localDateTimeToString(user.getTimeOfCreation()) + "' " +
                    "WHERE \"userID\" = '" + UUIDConverter.uuidToString(user.getId()) + "'");
            Log.d(TAG, "Updated user: " + user);
        });
    }

    @Override
    public void delete(User user) {
        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            statement.execute("DELETE FROM users " +
                    "WHERE \"userID\" = '" + UUIDConverter.uuidToString(user.getId()) + "'");
            Log.d(TAG, "Deleted user: " + user);
        });
    }

    @Override
    public User getByUsername(String username) {
        return JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            if (!rs.next()) {
                Log.d(TAG, "No user with username '" + username + "'");
                return null;
            }
            User user = new User(
                    UUIDConverter.uuidFromString(rs.getString("userID")),
                    rs.getString("username"),
                    rs.getString("password"),
                    LocalDateTimeConverter.localDateTimeFromString(rs.getString("creation_time"))
            );
            if (rs.next()) {
                Log.i(TAG, "There are more than one users with username '" + username + "'");
            }
            return user;
        });
    }
}
