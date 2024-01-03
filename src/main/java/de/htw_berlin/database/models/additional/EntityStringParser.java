package de.htw_berlin.database.models.additional;


import de.htw_berlin.database.models.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public final class EntityStringParser {

    public static final String SEPARATOR = "dkbKBj9dkjkKJd";
    public static final String USER = "user";
    /**
     * serializes category to string
     * @param user category
     * @return serialized string
     */
    public static String userToString(User user) {
        return USER + SEPARATOR
                + user.getId().toString() + SEPARATOR
                + user.getUsername() + SEPARATOR
                + user.getPassword() + SEPARATOR
                + user.getTimeOfCreation().toString();
    }

    /**
     * deserializes category from string
     * @param str string
     * @return deserialized category
     */
    public static User userFromString(String str) {
        String[] split = replaceNullStringWithNull(str.split(SEPARATOR));
        return new User(
                UUID.fromString(split[1]),
                split[2],
                split[3],
                LocalDateTime.parse(split[4])
        );
    }

    /**
     * If str equals 'null' returns <code>null</code>
     * @param str string
     * @return string or null
     */
    private static String replaceNullStringWithNull(String str) {
        return str == null || str.equals("null") ? null : str;
    }

    /**
     * Maps whole array to null if needed
     * @param strings string array
     * @return new string array with method {@link #replaceNullStringWithNull(String)} called on each entry
     */
    private static String[] replaceNullStringWithNull(String[] strings) {
        return Arrays.stream(strings)
                .map(EntityStringParser::replaceNullStringWithNull)
                .toArray(String[]::new);
    }

    /**
     * If str is null, returns String 'null' else returns str
     * @param str passed string
     * @return string or 'null'-string
     */
    private static String replaceNullWithNullString(String str) {
        return str == null ? "null" : str;
    }

}
