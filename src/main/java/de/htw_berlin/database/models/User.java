package de.htw_berlin.database.models;

import de.htw_berlin.database.validators.StringValidator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

    private UUID id;
    private String username;
    private String password;
    private LocalDateTime timeOfCreation;

    public User(UUID id, String username, String password, LocalDateTime timeOfCreation) throws IllegalArgumentException {
        this.id = id;
        setUsername(username);
        setPassword(password);
        setTimeOfCreation(timeOfCreation);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = StringValidator.validateUsername(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = StringValidator.validatePassword(password);
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = Objects.requireNonNull(timeOfCreation);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User that) {
            return this.id.equals(that.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" + username + "}";
    }

    public boolean equalsAllParams(User that) {
        return this.id.equals(that.id)
                && this.username.equals(that.username)
                && this.password.equals(that.password)
                && this.timeOfCreation.equals(that.timeOfCreation);
    }
}
