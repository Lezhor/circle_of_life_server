package de.htw_berlin.database.models.additional;

import java.util.UUID;

/**
 * offers method getId(). every entity implements this:
 * @see de.htw_berlin.database.models.Entity
 */
public interface HasId {

    /**
     * getter for id
     * @return id
     */
    UUID getId();

}
