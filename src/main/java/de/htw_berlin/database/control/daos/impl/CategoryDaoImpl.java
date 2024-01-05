package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.CategoryDao;
import de.htw_berlin.database.models.Category;

class CategoryDaoImpl implements CategoryDao {

    private static volatile CategoryDaoImpl instance;

    /**
     * Singleton getter
     * @return only existing instance of this class
     */
    static CategoryDaoImpl getInstance() {
        if (instance == null) {
            synchronized (CategoryDaoImpl.class) {
                if (instance == null) {
                    instance = new CategoryDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton getter
     */
    private CategoryDaoImpl() {
    }

    // TODO: 05.01.2024 implement all db methods

    @Override
    public void insert(Category entity) {

    }

    @Override
    public void update(Category entity) {

    }

    @Override
    public void delete(Category entity) {

    }
}
