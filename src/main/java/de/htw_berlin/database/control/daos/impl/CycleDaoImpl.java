package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.database.control.daos.CycleDao;
import de.htw_berlin.database.jdbc.JDBCController;
import de.htw_berlin.database.models.Cycle;
import de.htw_berlin.database.models.type_converters.CycleFrequencyConverter;
import de.htw_berlin.database.models.type_converters.UUIDConverter;
import de.htw_berlin.logging.Log;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

class CycleDaoImpl implements CycleDao {
    private static final String TAG = CycleDao.class.getSimpleName();

    private static volatile CycleDaoImpl instance;

    /**
     * Singleton getter
     * @return only existing instance of this class
     */
    static CycleDaoImpl getInstance() {
        if (instance == null) {
            synchronized (CycleDaoImpl.class) {
                if (instance == null) {
                    instance = new CycleDaoImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for singleton getter
     */
    private CycleDaoImpl() {
    }

    @Override
    public void insert(Cycle cycle) {
        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            statement.execute("INSERT INTO cycles (id, user_id, cycle_name, category_id, productiveness, frequency, archived) VALUES (" +
                    "'" + UUIDConverter.uuidToString(cycle.getId()) + "', " +
                    "'" + UUIDConverter.uuidToString(cycle.getUserID()) + "', " +
                    "'" + cycle.getName() + "', " +
                    "'" + UUIDConverter.uuidToString(cycle.getCategoryID()) + "', " +
                    "'" + CycleFrequencyConverter.frequecyToString(cycle.getFrequency()) + "', "
                    + cycle.isArchived() + ")");
            Log.d(TAG, "Inserted cycle: " + cycle);
        });
    }

    @Override
    public void update(Cycle cycle) {
        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            statement.execute("UPDATE cycles SET " +
                    "cycle_name = '" + cycle.getName() + "', " +
                    "category_id = '" + UUIDConverter.uuidToString(cycle.getCategoryID()) + "', " +
                    "productiveness = '" + CycleFrequencyConverter.frequecyToString(cycle.getFrequency()) + "', " +
                    "archived = " + cycle.isArchived() + " " +
                    "WHERE id = '" + UUIDConverter.uuidToString(cycle.getId()) + "'");
            Log.d(TAG, "Updated cycle: " + cycle);
        });
    }

    @Override
    public void delete(Cycle cycle) {
        JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            statement.execute("DELETE FROM cycles WHERE id = '" + UUIDConverter.uuidToString(cycle.getId()) + "'");
            Log.d(TAG, "Deleted cycle: " + cycle);
        });
    }

    @Override
    public Cycle getById(UUID id) {
        return JDBCController.executeInDB(con -> {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM cycles WHERE id = '" + UUIDConverter.uuidToString(id) + "'");
            if (!rs.next()) {
                return null;
            }
            return new Cycle(
                    UUIDConverter.uuidFromString(rs.getString("id")),
                    rs.getString("cycle_name"),
                    UUIDConverter.uuidFromString(rs.getString("user_id")),
                    UUIDConverter.uuidFromString(rs.getString("category_id")),
                    rs.getInt("productiveness"),
                    CycleFrequencyConverter.stringToFrequency(rs.getString("frequency")),
                    rs.getBoolean("archived")
            );
        });
    }
}
