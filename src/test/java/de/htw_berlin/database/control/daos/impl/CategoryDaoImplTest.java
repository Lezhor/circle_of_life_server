package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.application.App;
import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.models.Category;
import de.htw_berlin.database.models.User;
import de.htw_berlin.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDaoImplTest {

    User user;
    Category category;
    DatabaseController db = App.getDatabaseController();

    private void setUp() {
        Log.logToConsole(true);
        String username = "this_test_user_has_a_super_long_name_so_hopefully_nobody_takes_it_when_the_app_launches";
        try {
            db.clearUser(db.getUserByUsername(username));
        } catch (OperationNotSupportedException e) {
            fail(e);
        }
        user = new User(UUID.randomUUID(), username, "test_password", LocalDateTime.now());
        category = new Category(UUID.randomUUID(), "TestCategory", user.getUserID(), null);
        db.insert(user);
    }

    private void tearDown() {
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException e) {
            fail(e);
        }
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
        Log.logToConsole(false);
        assertDoesNotThrow(() -> db.insert(null));
        assertDoesNotThrow(() -> db.update(null));
        assertDoesNotThrow(() -> db.delete(null));
        assertDoesNotThrow(() -> db.exists(null));
        assertDoesNotThrow(() -> db.getById(UUID.randomUUID(), null));
    }
}