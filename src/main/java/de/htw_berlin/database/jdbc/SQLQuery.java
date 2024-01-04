package de.htw_berlin.database.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLQuery {

    /**
     * Executes a query on a given Connection instance
     * @param con connection instance
     * @throws SQLException if sql-query fails
     */
    void execute(Connection con) throws SQLException;

}
