package de.htw_berlin.database.control;

import de.htw_berlin.database.control.daos.*;
import de.htw_berlin.database.control.daos.impl.AppDatabaseImpl;

/**
 * offers getters for all daos
 */
public abstract class AppDatabase {

    static AppDatabase getInstance() {
        return AppDatabaseImpl.getInstance();
    }

    /**
     * Getter for {@link UserDao}.
     * @return UserDao
     */
    protected abstract UserDao getUserDao();

    protected abstract CategoryDao getCategoryDao();

    protected abstract CycleDao getCycleDao();

    protected abstract TodoDao getTodoDao();

    protected abstract AccomplishmentDao getAccomplishmentDao();

    protected abstract LogDao getLogDao();

}
