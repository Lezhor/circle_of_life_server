package de.htw_berlin.database.control;

import de.htw_berlin.application.App;
import de.htw_berlin.database.models.*;
import de.htw_berlin.database.models.additional.CycleFrequency;
import de.htw_berlin.logging.Log;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseControllerTest {
    private static final String TAG = DatabaseControllerTest.class.getSimpleName();

    /**
     * Username for testing. No one should ever take this one
     */
    public static final String USER_NAME = "another_long_username_which_hopefully_nobody_will_take323469";

    private final DatabaseController db = App.getDatabaseController();
    private User user;
    private Category category;
    private Cycle cycle;
    private Todo todo;
    private Accomplishment accomplishment;

    private void setUp() {
        Log.logToConsole(true);
        User oldUser = db.getUserByUsername(USER_NAME);
        if (oldUser != null) {
            try {
                db.clearUser(oldUser);
            } catch (OperationNotSupportedException e) {
                fail(e);
            }
        }
        user = new User(UUID.randomUUID(), USER_NAME, "this_is_a_password", LocalDateTime.now());
        category = new Category(UUID.randomUUID(), "TestCategory", user.getUserID(), null);
        cycle = new Cycle(UUID.randomUUID(), "TestCycle", user.getUserID(), category.getId(), 1, CycleFrequency.fromBinaryString("10010011"));
        todo = new Todo(UUID.randomUUID(), "TestTodo", user.getUserID(), category.getId(), -1, true, null);
        accomplishment = new Accomplishment(UUID.randomUUID(), user.getUserID(), cycle.getId(), null, "TestAccomplishment", null, 0, LocalDate.of(2022, 5, 3), null, 30_000);
        db.insert(user);
    }

    private void tearDown() {
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException ignored) {
        }
        Log.logToConsole(false);
    }

    @Test
    public void testInsertUserWithSameUsernameShouldFail() {
        setUp();
        Log.d(TAG, "Testing insert user with same username should fail");
        assertFalse(db.insert(new User(UUID.randomUUID(), USER_NAME, "another_password", LocalDateTime.now())));
        tearDown();
    }

    @Test
    public void testInsertCategory() {
        setUp();
        Log.d(TAG, "Test Insert Category");
        assertInsertWorks(category);
        assertTrue(category.equalsAllParams(db.getById(category.getId(), category.getClass())));
        tearDown();
    }

    @Test
    public void testInsertCycle() {
        setUp();
        Log.d(TAG, "Test Insert Cycle");
        db.insert(category);
        assertInsertWorks(cycle);
        assertTrue(cycle.equalsAllParams(db.getById(cycle.getId(), cycle.getClass())));
        tearDown();
    }

    @Test
    public void testInsertTodo() {
        setUp();
        Log.d(TAG, "Test Insert Todo");
        db.insert(category);
        assertInsertWorks(todo);
        assertTrue(todo.equalsAllParams(db.getById(todo.getId(), todo.getClass())));
        tearDown();
    }

    @Test
    public void testInsertAccomplishment() {
        setUp();
        Log.d(TAG, "Test Insert Todo");
        db.insert(category);
        db.insert(cycle);
        assertInsertWorks(accomplishment);
        assertTrue(accomplishment.equalsAllParams(db.getById(accomplishment.getId(), accomplishment.getClass())));
        tearDown();
    }

    private void assertInsertWorks(Entity entity) {
        assertFalse(db.exists(entity));
        assertTrue(db.insert(entity));
        assertTrue(db.exists(entity));
        assertFalse(db.insert(entity));
    }


}