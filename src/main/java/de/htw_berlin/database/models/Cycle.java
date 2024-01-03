package de.htw_berlin.database.models;

import de.htw_berlin.database.models.additional.Copyable;
import de.htw_berlin.database.models.additional.CycleFrequency;
import de.htw_berlin.database.validators.IntegerValidator;

import java.util.Objects;
import java.util.UUID;

/**
 * This Model-Class is an Entry for room-database. It represents the table `cycles`
 */
public class Cycle implements Entity, Copyable<Cycle> {

    private UUID id;
    private UUID userID;
    private String name;
    private UUID categoryID;

    /**
     * Value ranging from -1 to 1
     */
    private int productiveness;

    private CycleFrequency frequency;

    private boolean archived;

    /**
     * Constructor for cloning
     * @param that to be cloned
     */
    public Cycle(Cycle that) {
        this(that.id, that.name, that.userID, that.categoryID, that.productiveness, that.frequency, that.archived);
    }

    /**
     * Constructor for Cycle. Sets value archived to false.
     * @param name name
     * @param userID userID
     * @param categoryID category
     * @param productiveness productiveness
     * @param frequency frequency
     */
    public Cycle(UUID id, String name, UUID userID, UUID categoryID, int productiveness, CycleFrequency frequency) {
        this(id, name, userID, categoryID, productiveness, frequency, false);
    }

    public Cycle(UUID id, String name, UUID userID, UUID categoryID, int productiveness, CycleFrequency frequency, boolean archived) {
        this.id = id;
        this.name = name;
        this.userID = userID;
        this.categoryID = categoryID;
        setProductiveness(productiveness);
        this.frequency = frequency;
        this.archived = archived;
    }

    // GETTER AND SETTER

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

    public UUID getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(UUID categoryID) {
        this.categoryID = categoryID;
    }

    public int getProductiveness() {
        return productiveness;
    }

    public void setProductiveness(int productiveness) {
        this.productiveness = IntegerValidator.validateIntBetweenBounds(productiveness, -1, 1);
    }

    public CycleFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(CycleFrequency frequency) {
        this.frequency = frequency;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cycle that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }


    /**
     * Compares every attribute and returns true if all matching
     * @param that cycle
     * @return true if all attributes matching
     */
    public boolean equalsAllParams(Cycle that) {
        return this.userID.equals(that.userID)
                && this.id.equals(that.id)
                && this.name.equals(that.name)
                && Objects.equals(this.categoryID, that.categoryID)
                && this.productiveness == that.productiveness
                && this.frequency.equals(that.frequency)
                && this.archived == that.archived;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public Cycle copy() {
        return new Cycle(this);
    }
}
