package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.AccomplishmentDao;
import de.htw_berlin.database.models.Accomplishment;

import java.util.UUID;

class AccomplishmentDaoImpl implements AccomplishmentDao {

    private static volatile AccomplishmentDaoImpl instance;

    /**
     * Singleton getter
     * @return only existing instance of this class
     */
    static AccomplishmentDaoImpl getInstance() {
        if (instance == null) {
            synchronized (AccomplishmentDaoImpl.class) {
                if (instance == null) {
                    instance = new AccomplishmentDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor for singleton getter
     */
    private AccomplishmentDaoImpl() {
    }

    // TODO: 05.01.2024 implement db methods

    @Override
    public boolean insert(Accomplishment accomplishment) {
        return false;
    }

    @Override
    public boolean update(Accomplishment accomplishment) {
        return false;
    }

    @Override
    public boolean delete(Accomplishment accomplishment) {
        return false;
    }

    @Override
    public Accomplishment getById(UUID id) {
        return null;
    }
}
