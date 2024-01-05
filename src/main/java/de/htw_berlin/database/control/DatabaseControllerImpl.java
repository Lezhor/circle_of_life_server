package de.htw_berlin.database.control;

import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;

public class DatabaseControllerImpl implements DatabaseController {

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
        // TODO: 05.01.2024 Get correct Dao for log
    }

    @Override
    public void insertUser(User user) {
        db.getUserDao().insert(user);
    }

    @Override
    public void updateUser(User user) {
        db.getUserDao().update(user);
    }

    @Override
    public void deleteUser(User user) {
        db.getUserDao().delete(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return db.getUserDao().getByUsername(username);
    }

    @Override
    public void insertCategory(Category category) {
        db.getCategoryDao().insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        db.getCategoryDao().update(category);
    }

    @Override
    public void deleteCategory(Category category) {
        db.getCategoryDao().delete(category);
    }

    @Override
    public void insertCycle(Cycle cycle) {
        db.getCycleDao().insert(cycle);
    }

    @Override
    public void updateCycle(Cycle cycle) {
        db.getCycleDao().update(cycle);
    }

    @Override
    public void deleteCycle(Cycle cycle) {
        db.getCycleDao().delete(cycle);
    }

    @Override
    public void insertTodo(Todo todo) {
        db.getTodoDao().insert(todo);
    }

    @Override
    public void updateTodo(Todo todo) {
        db.getTodoDao().update(todo);
    }

    @Override
    public void deleteTodo(Todo todo) {
        db.getTodoDao().delete(todo);
    }

    @Override
    public void insertAccomplishment(Accomplishment accomplishment) {
        db.getAccomplishmentDao().insert(accomplishment);
    }

    @Override
    public void updateAccomplishment(Accomplishment accomplishment) {
        db.getAccomplishmentDao().update(accomplishment);
    }

    @Override
    public void deleteAccomplishment(Accomplishment accomplishment) {
        db.getAccomplishmentDao().delete(accomplishment);
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
