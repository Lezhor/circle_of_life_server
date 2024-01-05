package de.htw_berlin.database.control.daos;

import de.htw_berlin.engines.models.DBLog;

public interface LogDao {

    boolean insert(DBLog<?> log);

    boolean update(DBLog<?> log);

    boolean delete(DBLog<?> log);

}
