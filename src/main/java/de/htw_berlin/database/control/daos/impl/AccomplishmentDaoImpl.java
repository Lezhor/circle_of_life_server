package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.AccomplishmentDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.Accomplishment;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

class AccomplishmentDaoImpl implements AccomplishmentDao {

    private static volatile AccomplishmentDaoImpl instance;

    /**
     * Singleton getter
     *
     * @return only existing instance of this class
     */
    static AccomplishmentDaoImpl getInstance() {
        if (instance == null) {
            synchronized (AccomplishmentDaoImpl.class) {
                if (instance == null) {
                    instance = new AccomplishmentDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor for singleton getter
     */
    private AccomplishmentDaoImpl() {
    }

    @Override
    public boolean insertQuery(Accomplishment accomplishment) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute(("INSERT INTO accomplishments (id, user_id, cycle_id, todo_id, name, description, productiveness, date, timestamp, duration) VALUES (" +
                        "'" + UUIDConverter.uuidToString(accomplishment.getId()) + "', " +
                        "'" + UUIDConverter.uuidToString(accomplishment.getUserID()) + "', " +
                        "'" + UUIDConverter.uuidToString(accomplishment.getCycleID()) + "', " +
                        "'" + UUIDConverter.uuidToString(accomplishment.getTodoID()) + "', " +
                        "'" + accomplishment.getName() + "', " +
                        "'" + accomplishment.getDescription() + "', " +
                        accomplishment.getProductiveness() + ", " +
                        "'" + LocalDateTimeConverter.localDateToString(accomplishment.getDate()) + "', " +
                        "'" + LocalDateTimeConverter.localTimeToString(accomplishment.getTimestamp()) + "', " +
                        accomplishment.getDurationMillis() + ")").replaceAll("'null'", "NULL"));
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public boolean updateQuery(Accomplishment accomplishment) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute(("UPDATE accomplishments SET " +
                        "cycle_id = '" + UUIDConverter.uuidToString(accomplishment.getCycleID()) + "', " +
                        "todo_id = '" + UUIDConverter.uuidToString(accomplishment.getTodoID()) + "', " +
                        "name = '" + accomplishment.getName() + "', " +
                        "description = '" + accomplishment.getDescription() + "', " +
                        "productiveness = " + accomplishment.getProductiveness() + ", " +
                        "date = '" + LocalDateTimeConverter.localDateToString(accomplishment.getDate()) + "', " +
                        "timestamp = '" + LocalDateTimeConverter.localTimeToString(accomplishment.getTimestamp()) + "', " +
                        "duration = " + accomplishment.getDurationMillis() +
                        " WHERE id = '" + UUIDConverter.uuidToString(accomplishment.getId()) + "'").replaceAll("'null'", "NULL"));
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public boolean deleteQuery(Accomplishment accomplishment) {
        return Boolean.TRUE.equals(JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                statement.execute("DELETE FROM accomplishments " +
                        "WHERE id = '" + UUIDConverter.uuidToString(accomplishment.getId()) + "'");
                return true;
            } catch (SQLException e) {
                return false;
            }
        }));
    }

    @Override
    public Accomplishment getById(UUID id) {
        return JDBCController.executeInDB(con -> {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM accomplishments " +
                        "WHERE id = '" + UUIDConverter.uuidToString(id) + "'");
                if (!rs.next()) {
                    return null;
                }
                return new Accomplishment(
                        UUIDConverter.uuidFromString(rs.getString("id")),
                        UUIDConverter.uuidFromString(rs.getString("user_id")),
                        UUIDConverter.uuidFromString(rs.getString("cycle_id")),
                        UUIDConverter.uuidFromString(rs.getString("todo_id")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("productiveness"),
                        LocalDateTimeConverter.localDateFromString(rs.getString("date")),
                        LocalDateTimeConverter.localTimeFromString(rs.getString("timestamp")),
                        rs.getBigDecimal("duration").longValue()
                );
            } catch (SQLException e) {
                return null;
            }
        });
    }
}
