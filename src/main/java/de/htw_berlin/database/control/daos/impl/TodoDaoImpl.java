package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.TodoDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.Todo;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public boolean insert(Todo todo) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("INSERT INTO todos (id, user_id, todo_name, category_id, productiveness, done, due_date) VALUES (" +
                        "'" + UUIDConverter.uuidToString(todo.getId()) + "', " +
                        "'" + UUIDConverter.uuidToString(todo.getUserID()) + "', " +
                        "'" + todo.getName() + "', " +
                        "'" + UUIDConverter.uuidToString(todo.getCategoryID()) + "', " +
                        todo.getProductive() + ", " +
                        todo.isDone() + ", " +
                        "'" + LocalDateTimeConverter.localDateTimeToString(todo.getDueDate()) + "')");
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public boolean update(Todo todo) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("UPDATE todos SET " +
                        "todo_name = '" + todo.getName() + "', " +
                        "category_id = '" + UUIDConverter.uuidToString(todo.getCategoryID()) + "', " +
                        "productiveness = " + todo.getProductive() + ", " +
                        "done = " + todo.isDone() + ", " +
                        "due_date = '" + LocalDateTimeConverter.localDateTimeToString(todo.getDueDate()) + "' " +
                        "WHERE id = '" + UUIDConverter.uuidToString(todo.getId()) + "'");
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public boolean delete(Todo todo) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("DELETE FROM todos WHERE id = '" + UUIDConverter.uuidToString(todo.getId()) + "'");
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public Todo getById(UUID id) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM todos WHERE id = '" + UUIDConverter.uuidToString(id) + "'");
                if (!rs.next()) {
                    return null;
                }
                return new Todo(
                        UUIDConverter.uuidFromString(rs.getString("id")),
                        rs.getString("todo_name"),
                        UUIDConverter.uuidFromString(rs.getString("user_id")),
                        UUIDConverter.uuidFromString(rs.getString("category_id")),
                        rs.getInt("productiveness"),
                        rs.getBoolean("done"),
                        LocalDateTimeConverter.localDateTimeFromString(rs.getString("due_date"))
                );
            } catch (SQLException e) {
                return null;
            }
        });
    }
}
