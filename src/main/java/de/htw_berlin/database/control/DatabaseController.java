package de.htw_berlin.database.control;

import de.htw_berlin.database.models.*;
import de.htw_berlin.engines.models.DBLog;

/**
 * Methods for changing data
 */
public interface DatabaseController {

    <E extends Entity> void executeLog(DBLog<E> log);


    // Users

    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(User user);

    /**
     * Searches for user with given username in database
     * @param username username
     * @return user if found oro null if not found
     */
    User getUserByUsername(String username);


    // Categories

    void insertCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Category category);


    // Cycles

    void insertCycle(Cycle cycle);
    void updateCycle(Cycle cycle);
    void deleteCycle(Cycle cycle);


    // Todos

    void insertTodo(Todo todo);
    void updateTodo(Todo todo);
    void deleteTodo(Todo todo);


    // Accomplishments

    void insertAccomplishment(Accomplishment accomplishment);
    void updateAccomplishment(Accomplishment accomplishment);
    void deleteAccomplishment(Accomplishment accomplishment);


    // Logs

    void insertLog(DBLog<?> log);
    void updateLog(DBLog<?> log);
    void deleteLog(DBLog<?> log);



}
