package de.htw_berlin.database.control.daos;

import de.htw_berlin.database.models.User;
import de.htw_berlin.engines.models.DBLog;

import java.time.LocalDateTime;
import java.util.List;

public interface LogDao {

    boolean insert(DBLog<?> log, LocalDateTime timestamp);

    boolean update(DBLog<?> log);

    boolean delete(DBLog<?> log);

    List<DBLog<?>> getLogsBetweenTimestamps(User client, LocalDateTime timestamp1, LocalDateTime timestamp2);
}
