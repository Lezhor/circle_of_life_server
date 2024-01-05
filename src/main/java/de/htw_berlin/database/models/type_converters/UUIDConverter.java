package de.htw_berlin.database.models.type_converters;

import java.util.UUID;

/**
 * Converts UUID to string and back.
 */
public class UUIDConverter {

    /**
     * Converts UUID to String. null maps to null
     * @param uuid uuid
     * @return String representation of uuid
     */
    public static String uuidToString(UUID uuid) {

        return uuid == null ? null : uuid.toString();
    }

    /**
     * Converts String to UUID. null maps to null
     * @param str string representation of uuid
     * @return uuid
     */
    public static UUID uuidFromString(String str) {
        return str == null || str.equalsIgnoreCase("null") ? null : UUID.fromString(str);
    }

}
