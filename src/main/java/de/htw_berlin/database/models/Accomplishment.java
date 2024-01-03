package de.htw_berlin.database.models;

import de.htw_berlin.database.models.additional.Copyable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * This Model-Class is an Entry for room-database. It represents the table `accomplishments`<br>
 * Its primary key is `userID, a_id`, and it has two foreign keys. One to the table `cycleID` one to the table `todos`
 */
public class Accomplishment implements Entity, Copyable<Accomplishment> {

    private UUID id;

    private UUID userID;

    private UUID cycleID;

    private UUID todoID;

    private String name;

    private String description;

    private int productiveness;

    private LocalDate date;

    private LocalTime timestamp;

    private long durationMillis;

    /**
     * Constructor for cloning
     * @param that to be cloned
     */
    public Accomplishment(Accomplishment that) {
        this(that.id, that.userID, that.cycleID, that.todoID, that.name, that.description, that.productiveness, that.date, that.timestamp, that.durationMillis);
    }

    public Accomplishment(UUID id, UUID userID, UUID cycleID, UUID todoID, String name, String description, int productiveness, LocalDate date, LocalTime timestamp, long durationMillis) {
        this.id = id;
        this.userID = userID;
        this.cycleID = cycleID;
        this.todoID = todoID;
        this.name = name;
        this.description = description;
        this.productiveness = productiveness;
        this.date = date;
        this.timestamp = timestamp;
        this.durationMillis = durationMillis;
    }

    /**
     * Returns localdatetime. If time is not set, the time will be 00:00:00
     * @return dateTime
     */
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(date, timestamp != null ? timestamp : LocalTime.of(0, 0, 0));
    }

    public void setLocalDateTime(LocalDateTime dateTime) {
        this.date = dateTime.toLocalDate();
        this.timestamp = dateTime.toLocalTime();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getCycleID() {
        return cycleID;
    }

    public void setCycleID(UUID cycleID) {
        this.cycleID = cycleID;
    }

    public UUID getTodoID() {
        return todoID;
    }

    public void setTodoID(UUID todoID) {
        this.todoID = todoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductiveness() {
        return productiveness;
    }

    public void setProductiveness(int productiveness) {
        this.productiveness = productiveness;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Accomplishment that) {
            return this.userID.equals(that.userID) && this.id.equals(that.id);
        }
        return false;
    }

    public boolean equalsAllParams(Accomplishment that) {
        return this.id.equals(that.id)
                && this.userID.equals(that.userID)
                && Objects.equals(this.cycleID, that.cycleID)
                && Objects.equals(this.todoID, that.todoID)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.description, that.description)
                && this.productiveness == that.productiveness
                && Objects.equals(this.date, that.date)
                && Objects.equals(this.timestamp, that.timestamp)
                && this.durationMillis == that.durationMillis;
    }

    @Override
    public Accomplishment copy() {
        return new Accomplishment(this);
    }
}
