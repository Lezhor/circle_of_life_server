package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.CategoryDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.Category;
import de.htw_berlin.database.models.type_converters.UUIDConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

class CategoryDaoImpl implements CategoryDao {

    private static volatile CategoryDaoImpl instance;

    /**
     * Singleton getter
     *
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

    @Override
    public boolean insert(Category category) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("INSERT INTO categories (id, user_id, category_name, parent_id) VALUES (" +
                        "'" + UUIDConverter.uuidToString(category.getId()) + "', " +
                        "'" + UUIDConverter.uuidToString(category.getUserID()) + "', " +
                        "'" + category.getName() + "', " +
                        "'" + UUIDConverter.uuidToString(category.getParentID()) + "')");
                return true;
            } catch (SQLException e) {
                return false;
            }
        });
    }

    @Override
    public boolean update(Category category) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("UPDATE categories SET " +
                        "category_name = '" + category.getName() + "', " +
                        "parent_id = '" + UUIDConverter.uuidToString(category.getParentID()) + "', " +
                        "WHERE id = '" + UUIDConverter.uuidToString(category.getId()) + "'");
                return true;
            } catch (SQLException e) {
                return false;
            }
        });
    }

    @Override
    public boolean delete(Category category) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("DELETE FROM categories WHERE id = '" + UUIDConverter.uuidToString(category.getId()) + "'");
                return true;
            } catch (SQLException e) {
                return false;
            }
        });
    }

    @Override
    public Category getById(UUID id) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM categories WHERE id = '" + UUIDConverter.uuidToString(id) + "'");
                if (!rs.next()) {
                    return null;
                }
                return new Category(
                        UUIDConverter.uuidFromString(rs.getString("id")),
                        rs.getString("category_name"),
                        UUIDConverter.uuidFromString(rs.getString("user_id")),
                        UUIDConverter.uuidFromString(rs.getString("parent_id"))
                );
            } catch (SQLException e) {
                return null;
            }
        });
    }
}
