package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.TodoDao;
import de.htw_berlin.database.models.Todo;

import java.util.UUID;

class TodoDaoImpl implements TodoDao {

    private static volatile TodoDaoImpl instance;

    /**
     * singleton getter
     * @return only existing instance of this class
     */
    static TodoDaoImpl getInstance() {
        if (instance == null) {
            synchronized (TodoDaoImpl.class) {
                if (instance == null) {
                    instance = new TodoDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * constructor for singleton getter
     */
    private TodoDaoImpl() {
    }

    // TODO: 05.01.2024 Implement db methods

    @Override
    public boolean insert(Todo todo) {
        return false;
    }

    @Override
    public boolean update(Todo todo) {
        return false;
    }

    @Override
    public boolean delete(Todo todo) {
        return false;
    }

    @Override
    public Todo getById(UUID id) {
        return null;
    }
}
