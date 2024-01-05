package de.htw_berlin.database.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLQueryWithResult<T> {

    /**
     * Executes a query on a given Connection instance
     * @param con connection instance
     * @return result of type t
     * @throws SQLException if something fails
     */
    T execute(Connection con) throws SQLException;

}
