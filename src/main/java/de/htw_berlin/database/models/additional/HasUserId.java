package de.htw_berlin.database.models.additional;

import java.util.UUID;

/**
 * Every model implements this interface. It offers just the getUserId() method
 */
public interface HasUserId {

    /**
     * Getter for the userId
     * @return userId
     */
    UUID getUserID();
}
