package de.htw_berlin.database.control.daos;

import de.htw_berlin.engines.models.DBLog;

public interface LogDao {

    void insert(DBLog<?> log);

    void update(DBLog<?> log);

    void delete(DBLog<?> log);

}
