package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.LogDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.User;
import de.htw_berlin.database.models.type_converters.DBLogConverter;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.logging.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

class LogDaoImpl implements LogDao {
    private static final String TAG = LogDao.class.getSimpleName();

    private static volatile LogDaoImpl instance;

    /**
     * singleton getter
     * @return only existing instance of this class
     */
    static LogDaoImpl getInstance() {
        if (instance == null) {
            synchronized (LogDaoImpl.class) {
                if (instance == null) {
                    instance = new LogDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * constructor for singleton getter
     */
    private LogDaoImpl() {
    }

    @Override
    public boolean insert(DBLog<?> log) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute(("INSERT INTO logs (id, user_id, timestamp, content) VALUES (" +
                        "'" + UUIDConverter.uuidToString(log.getId()) + "', " +
                        "'" + UUIDConverter.uuidToString(log.getUserId()) + "', " +
                        "'" + LocalDateTimeConverter.localDateTimeToString(log.getTimestamp()) + "', " +
                        "'" + DBLogConverter.dbLogToString(log) + "')").replaceAll("'null'", "NULL"));
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public boolean update(DBLog<?> log) {
        Log.w(TAG, "Update Operation is not supported!!!");
        return false;
    }

    @Override
    public boolean delete(DBLog<?> log) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("DELETE FROM logs WHERE id = '" + UUIDConverter.uuidToString(log.getId()) + "'");
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public List<DBLog<?>> getLogsBetweenTimestamps(User client, LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                List<DBLog<?>> logs = new LinkedList<>();

                ResultSet rs = statement.executeQuery("SELECT * FROM logs WHERE " +
                        "user_id = '" + UUIDConverter.uuidToString(client.getUserID()) + "' AND " +
                        "timestamp >= '" + LocalDateTimeConverter.localDateTimeToString(timestamp1) + "' AND " +
                        "timestamp < '" + LocalDateTimeConverter.localDateTimeToString(timestamp2) + "' " +
                        "ORDER BY timestamp");
                while (rs.next()) {
                    logs.add(DBLogConverter.stringToDBLog(rs.getString("content")));
                }
                return logs;
            } catch (SQLException e) {
                return null;
            }
        });
    }

    /**
     * returns all logs of a user
     * @param user user
     * @return logs
     */
    List<DBLog<?>> getLogs(User user) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                List<DBLog<?>> logs = new LinkedList<>();

                ResultSet rs = statement.executeQuery("SELECT * FROM logs WHERE " +
                        "user_id = '" + UUIDConverter.uuidToString(user.getUserID()) + "' " +
                        "ORDER BY timestamp");
                while (rs.next()) {
                    logs.add(DBLogConverter.stringToDBLog(rs.getString("content")));
                }
                return logs;
            } catch (SQLException e) {
                return null;
            }
        });
    }
}
