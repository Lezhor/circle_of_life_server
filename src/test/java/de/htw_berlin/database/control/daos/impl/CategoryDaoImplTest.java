package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.application.App;
import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.models.Category;
import de.htw_berlin.database.models.User;
import de.htw_berlin.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDaoImplTest {

    User user;
    Category category;
    DatabaseController db = App.getDatabaseController();

    private void setUp() {
        Log.logToConsole(true);
        user = new User(UUID.randomUUID(), "this_test_user_has_a_super_long_name_so_hopefully_nobody_takes_it_when_the_app_launches", "test_password", LocalDateTime.now());
        category = new Category(UUID.randomUUID(), "TestCategory", user.getUserID(), null);
        db.insert(user);
    }

    private void tearDown() {
        db.delete(category);
        db.delete(user);
    }

    @Test
    public void testInsert() {
        setUp();
        assertTrue(db.exists(user));
        assertFalse(db.exists(category));
        db.insert(category);
        assertTrue(db.exists(category));
        tearDown();
    }

    @Test
    public void testNullParamsShouldNotThrowException() {
        assertDoesNotThrow(() -> db.insert(null));
        assertDoesNotThrow(() -> db.update(null));
        assertDoesNotThrow(() -> db.delete(null));
        assertDoesNotThrow(() -> db.exists(null));
        assertDoesNotThrow(() -> db.getById(UUID.randomUUID(), null));
    }
}