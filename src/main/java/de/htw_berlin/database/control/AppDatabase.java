package de.htw_berlin.database.control;

import de.htw_berlin.database.control.daos.UserDao;
import de.htw_berlin.database.control.daos.impl.UserDaoImpl;

public class AppDatabase {

    public static UserDao getUserDao() {
        return UserDaoImpl.getInstance();
    }

}
