package de.htw_berlin.database.models;

import de.htw_berlin.database.models.additional.Copyable;
import de.htw_berlin.database.models.additional.HasId;
import de.htw_berlin.database.models.additional.HasUserId;

/**
 * Container for all interfaces an entity should implement
 */
public interface Entity extends HasId, HasUserId {

}
