package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.LogDao;
import de.htw_berlin.engines.models.DBLog;

class LogDaoImpl implements LogDao {

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

    // TODO: 05.01.2024 implement all db methods

    @Override
    public void insert(DBLog<?> log) {

    }

    @Override
    public void update(DBLog<?> log) {

    }

    @Override
    public void delete(DBLog<?> log) {

    }
}
