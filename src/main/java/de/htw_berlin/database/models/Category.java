package de.htw_berlin.database.models;

import de.htw_berlin.database.models.additional.Copyable;
import de.htw_berlin.database.validators.StringValidator;

import java.util.Objects;
import java.util.UUID;

/**
 * This Model-Class is an Entry for database. It represents the table `categories`
 */
public class Category implements Entity, Copyable<Category> {

    private UUID id;

    private UUID userID;

    private String name;

    private UUID parentID;

    /**
     * Constructor for cloning
     * @param that other category
     */
    public Category(Category that) {
        this(that.id, that.name, that.userID, that.parentID);
    }

    public Category(UUID id, String name, UUID userID, UUID parentID) {
        this.id = id;
        this.name = StringValidator.validateStringMinLength(name, 1);
        this.userID = userID;
        this.parentID = parentID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getParentID() {
        return parentID;
    }

    public void setParentID(UUID parent) {
        if (!this.id.equals(parent))
            this.parentID = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }

    /**
     * Compares every attribute and returns true if all matching
     *
     * @param that category
     * @return true if all attributes matching
     */
    public boolean equalsAllParams(Category that) {
        return this.userID.equals(that.userID)
                && this.id.equals(that.id)
                && this.name.equals(that.name)
                && Objects.equals(this.parentID, that.parentID);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Category[" + getName() + "]";
    }

    @Override
    public Category copy() {
        return new Category(this);
    }
}
