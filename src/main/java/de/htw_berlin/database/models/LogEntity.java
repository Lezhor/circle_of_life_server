package de.htw_berlin.database.models;

import de.htw_berlin.database.models.additional.Copyable;
import de.htw_berlin.engines.models.DBLog;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This Model-Class is an Entry for database. It represents the table `logs`<br>
 * Its primary key is `userID, timestamp`. It means you can't have two logs with the same timestamp of creation.
 * However, this shouldn't be possible anyway since they would need to be created in the same millisecond...
 */
public class LogEntity implements Entity, Copyable<LogEntity> {

    private UUID id;

    private UUID userID;

    private LocalDateTime timestamp;

    private DBLog<?> log;

    public LogEntity(DBLog<?> log) {
        this(log.getId(), log.getUserId(), log.getTimestamp(), log);
    }

    LogEntity(UUID id, UUID userID, LocalDateTime timestamp, DBLog<?> log) {
        this.id = id;
        this.userID = userID;
        this.timestamp = timestamp;
        this.log = log;
    }

    @Override
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public DBLog<?> getLog() {
        return log;
    }

    public void setLog(DBLog<?> log) {
        this.log = log;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public LogEntity copy() {
        return new LogEntity(this.id, this.userID, this.timestamp, this.log);
    }
}
