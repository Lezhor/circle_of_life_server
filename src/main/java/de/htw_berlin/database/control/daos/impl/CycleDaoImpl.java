package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.CycleDao;
import de.htw_berlin.database.models.Cycle;

class CycleDaoImpl implements CycleDao {

    private static volatile CycleDaoImpl instance;

    /**
     * Singleton getter
     * @return only existing instance of this class
     */
    static CycleDaoImpl getInstance() {
        if (instance == null) {
            synchronized (CycleDaoImpl.class) {
                if (instance == null) {
                    instance = new CycleDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton getter
     */
    private CycleDaoImpl() {
    }

    // TODO: 05.01.2024 implement db methods

    @Override
    public void insert(Cycle entity) {

    }

    @Override
    public void update(Cycle entity) {

    }

    @Override
    public void delete(Cycle entity) {

    }
}
