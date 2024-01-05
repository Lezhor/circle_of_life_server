package de.htw_berlin.database.control;

import de.htw_berlin.database.control.daos.*;
import de.htw_berlin.database.control.daos.impl.AppDatabaseImpl;
import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;

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

    /**
     * Finds dao which fits the given entity
     * @param entity entity
     * @return BaseDao of type E
     * @param <E> type of the entity
     */
    <E extends Entity> BaseDao<E> getDao(E entity) {
        return entity == null ? null : (BaseDao<E>) getDao(entity.getClass());
    }

    <E extends Entity> BaseDao<E> getDao(Class<E> entityClass) {
        if (entityClass == null) {
            return null;
        } else if (entityClass.equals(User.class)) {
            return (BaseDao<E>) getUserDao();
        } else if (entityClass.equals(Category.class)) {
            return (BaseDao<E>) getCategoryDao();
        } else if (entityClass.equals(Cycle.class)) {
            return (BaseDao<E>) getCycleDao();
        } else if (entityClass.equals(Todo.class)) {
            return (BaseDao<E>) getTodoDao();
        } else if (entityClass.equals(Accomplishment.class)) {
            return (BaseDao<E>) getAccomplishmentDao();
        } else {
            return null;
        }
    }


}
