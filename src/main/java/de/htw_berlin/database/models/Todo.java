package de.htw_berlin.database.models;

import de.htw_berlin.database.models.additional.Copyable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


/**
 * This Model-Class is an Entry for database. It represents the table `todos`
 */
public class Todo implements Entity, Copyable<Todo> {
    private UUID id;

    private String name;

    private UUID userID;

    private UUID categoryID;

    private int productive;

    private boolean done;

    private LocalDateTime dueDate;

    /**
     * Constructor for cloning todó
     * @param that to be cloned
     */
    public Todo(Todo that) {
        this(that.id, that.name, that.userID, that.categoryID, that.productive, that.done, that.dueDate);
    }

    public Todo(UUID id, String name, UUID userID, UUID categoryID, int productive) {
        this(id, name, userID, categoryID, productive, false, null);
    }

    public Todo(UUID id, String name, UUID userID, UUID categoryID, int productive, LocalDateTime dueDate) {
        this(id, name, userID, categoryID, productive, false, dueDate);
    }

    public Todo(UUID id, String name, UUID userID, UUID categoryID, int productive, boolean done, LocalDateTime dueDate) {
        this.id = id;
        this.name = name;
        this.userID = userID;
        this.categoryID = categoryID;
        this.productive = productive;
        this.done = done;
        this.dueDate = dueDate;
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

    public UUID getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(UUID categoryID) {
        this.categoryID = categoryID;
    }

    public int getProductive() {
        return productive;
    }

    public void setProductive(int productive) {
        this.productive = productive;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Todo that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }

    /**
     * Compares every attribute and returns true if all matching
     * @param that todô
     * @return true if all attributes matching
     */
    public boolean equalsAllParams(Todo that) {
        return this.userID.equals(that.userID)
                && this.id.equals(that.id)
                && this.name.equals(that.name)
                && Objects.equals(this.categoryID, that.categoryID)
                && this.done == that.done
                && this.productive == that.productive
                && Objects.equals(this.dueDate, that.dueDate);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public Todo copy() {
        return new Todo(this);
    }
}
