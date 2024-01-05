package de.htw_berlin.database.jdbc;

import java.sql.Connection;

@FunctionalInterface
public interface SQLQueryWithResult<T> {

    /**
     * Executes a query on a given Connection instance
     * @param con connection instance
     * @return result of type t
     */
    T execute(Connection con);

}
