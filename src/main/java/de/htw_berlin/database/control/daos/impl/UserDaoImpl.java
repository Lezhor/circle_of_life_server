package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.UserDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.User;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;
import de.htw_berlin.logging.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

class UserDaoImpl implements UserDao {
    private static final String TAG = UserDao.class.getSimpleName();

    private static volatile UserDaoImpl instance;

    /**
     * getter for singleton pattern
     * @return only existing instance of this class
     */
    static UserDao getInstance() {
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
    public boolean insert(User user) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("INSERT INTO users (user_id, username, password, creation_time) VALUES (" +
                        "'" + UUIDConverter.uuidToString(user.getUserID()) + "', " +
                        "'" + user.getUsername() + "', " +
                        "'" + user.getPassword() + "', " +
                        "'" + LocalDateTimeConverter.localDateTimeToString(user.getTimeOfCreation()) + "')");
            } catch (SQLException e) {
                return false;
            }
            return true;
        }));
    }

    @Override
    public boolean update(User user) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("UPDATE users SET " +
                        "username = '" + user.getUsername() + "', " +
                        "password = '" + user.getPassword() + "', " +
                        "creation_time = '" + LocalDateTimeConverter.localDateTimeToString(user.getTimeOfCreation()) + "' " +
                        "WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
            } catch (SQLException e) {
                return false;
            }
            return true;
        }));
    }

    @Override
    public boolean delete(User user) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("DELETE FROM users " +
                        "WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                Log.d(TAG, "Deleted user: " + user);
            } catch (SQLException e) {
                return false;
            }
            return true;
        }));
    }

    @Override
    public User getById(UUID id) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE user_id = '" + UUIDConverter.uuidToString(id) + "'");
                if (!rs.next()) {
                    Log.d(TAG, "No user with found with id '" + UUIDConverter.uuidToString(id) + "'");
                    return null;
                }
                return new User(
                        UUIDConverter.uuidFromString(rs.getString("user_id")),
                        rs.getString("username"),
                        rs.getString("password"),
                        LocalDateTimeConverter.localDateTimeFromString(rs.getString("creation_time"))
                );
            } catch (SQLException e) {
                return null;
            }
        });
    }

    @Override
    public User getByUsername(String username) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
                if (!rs.next()) {
                    return null;
                }
                User user = new User(
                        UUIDConverter.uuidFromString(rs.getString("user_id")),
                        rs.getString("username"),
                        rs.getString("password"),
                        LocalDateTimeConverter.localDateTimeFromString(rs.getString("creation_time"))
                );
                if (rs.next()) {
                    Log.i(TAG, "There are more than one users with username '" + username + "'");
                }
                return user;
            } catch (SQLException e) {
                return null;
            }
        });
    }

    @Override
    public boolean clearUser(User user) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("DELETE FROM logs WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                statement.execute("DELETE FROM accomplishments WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                statement.execute("DELETE FROM todos WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                statement.execute("DELETE FROM cycles WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                statement.execute("DELETE FROM categories WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                statement.execute("DELETE FROM users WHERE user_id = '" + UUIDConverter.uuidToString(user.getId()) + "'");
                Log.d(TAG, "Cleared user: " + user);
            } catch (SQLException e) {
                return false;
            }
            return true;
        }));
    }
}
