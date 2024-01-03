package de.htw_berlin.database.models.type_converters;


import de.htw_berlin.engines.models.DBLog;

/**
 * Converts String to DBLog and back. Used in Room Database.<br><br>
 * The most important feature of this converter is: When you plug output of a method in the other method you land where you started. Both ways.
 */
public class DBLogConverter {

    /**
     * Converts DBLog to String representation
     * @param log Log
     * @return String representation
     */
    public static String dbLogToString(DBLog<?> log) {
        return DBLog.toString(log);
    }

    /**
     * Converts String to log
     * @param str String representation of log
     * @return converted log
     */
    public static DBLog<?> stringToDBLog(String str) {
        return DBLog.fromString(str);
    }

}
