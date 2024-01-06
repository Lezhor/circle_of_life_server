package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.LogDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.type_converters.DBLogConverter;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.logging.Log;

import java.sql.SQLException;
import java.sql.Statement;

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
                statement.execute("INSERT INTO logs (id, user_id, timestamp, content) VALUES (" +
                        "'" + UUIDConverter.uuidToString(log.getId()) + "', " +
                        "'" + UUIDConverter.uuidToString(log.getUserId()) + "', " +
                        "'" + LocalDateTimeConverter.localDateTimeToString(log.getTimestamp()) + "', " +
                        "'" + DBLogConverter.dbLogToString(log) + "')");
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
}
