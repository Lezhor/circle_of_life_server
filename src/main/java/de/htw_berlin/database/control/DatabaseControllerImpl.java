package de.htw_berlin.database.control;

import de.htw_berlin.database.control.daos.BaseDao;
import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.logging.Log;

import javax.naming.OperationNotSupportedException;
import java.util.UUID;

public class DatabaseControllerImpl implements DatabaseController {
    private static final String TAG = DatabaseController.class.getSimpleName();
    private static final boolean CLEARING_USER_ENABLED = true; // TODO: 05.01.2024 Disable for final release!

    private static volatile DatabaseControllerImpl instance;

    /**
     * Singleton getter
     * @return only existing instance of this class
     */
    public static DatabaseControllerImpl getInstance() {
        if (instance == null) {
            synchronized (DatabaseControllerImpl.class) {
                if (instance == null) {
                    instance = new DatabaseControllerImpl();
                }
            }
        }
        return instance;
    }


    private final AppDatabase db;

    private DatabaseControllerImpl() {
        db = AppDatabase.getInstance();
    }

    @Override
    public <E extends Entity> void executeLog(DBLog<E> log) {
        BaseDao<E> dao = db.getDao(log.getChangedObject());
        if (dao == null) {
            Log.w(TAG, "Couldn't find dao for log-type " + log.getChangedObject().getClass().getName());
        }
    }

    @Override
    public <E extends Entity> void insert(E entity) {
        BaseDao<E> dao = db.getDao(entity);
        if (dao == null) {
            Log.w(TAG, "Couldn't find dao for entity " + entity);
        } else {
            dao.insert(entity);
        }
    }

    @Override
    public <E extends Entity> void update(E entity) {
        BaseDao<E> dao = db.getDao(entity);
        if (dao == null) {
            Log.w(TAG, "Couldn't find dao for entity " + entity);
        } else {
            dao.update(entity);
        }
    }

    @Override
    public <E extends Entity> void delete(E entity) {
        BaseDao<E> dao = db.getDao(entity);
        if (dao == null) {
            Log.w(TAG, "Couldn't find dao for entity " + entity);
        } else {
            dao.delete(entity);
        }
    }

    @Override
    public <E extends Entity> E getById(UUID id, Class<E> entityClass) {
        BaseDao<E> dao = db.getDao(entityClass);
        if (dao == null) {
            Log.w(TAG, "Couldn't find dao for entity-class " + (entityClass == null ? null : entityClass.getName()));
            return null;
        } else {
            return dao.getById(id);
        }
    }

    @Override
    public <E extends Entity> boolean exists(E entity) {
        BaseDao<E> dao = db.getDao(entity);
        if (dao == null) {
            Log.w(TAG, "Couldn't find dao for entity " + entity);
            return false;
        } else {
            return dao.exists(entity);
        }
    }

    @Override
    public void clearUser(User user) throws OperationNotSupportedException {
        // TODO: 05.01.2024
        if (CLEARING_USER_ENABLED) {
            if (user != null) {
                db.getUserDao().clearUser(user);
            }
        } else {
            throw new OperationNotSupportedException("Clearing users data is disabled!!!");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return db.getUserDao().getByUsername(username);
    }

    @Override
    public void insertLog(DBLog<?> log) {
        db.getLogDao().insert(log);
    }

    @Override
    public void updateLog(DBLog<?> log) {
        db.getLogDao().update(log);
    }

    @Override
    public void deleteLog(DBLog<?> log) {
        db.getLogDao().delete(log);
    }
}
