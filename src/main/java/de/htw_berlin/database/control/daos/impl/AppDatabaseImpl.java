package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.AppDatabase;
import de.htw_berlin.database.control.daos.UserDao;

public class AppDatabaseImpl extends AppDatabase {

    private static volatile AppDatabaseImpl instance;

    public static AppDatabaseImpl getInstance() {
        if (instance == null) {
            synchronized (AppDatabaseImpl.class) {
                if (instance == null) {
                    instance = new AppDatabaseImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton
     */
    private AppDatabaseImpl() {
    }

    @Override
    protected UserDao getUserDao() {
        return UserDaoImpl.getInstance();
    }

}
