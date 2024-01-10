package de.htw_berlin.database.control.daos.impl;

import de.htw_berlin.application.App;
import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.models.Category;
import de.htw_berlin.database.models.Cycle;
import de.htw_berlin.database.models.User;
import de.htw_berlin.database.models.additional.CycleFrequency;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.logging.Log;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LogDaoImplTest {

    private final DatabaseController db = App.getDatabaseController();
    private LogDaoImpl dao;

    public static final String USER_NAME = "teeeest_sdk_a_username_whichhopefully_nobody_will_take";
    User user;
    DBLog<?>[] logs;

    private void setUp() {
        Log.logToConsole(true);
        dao = LogDaoImpl.getInstance();
        user = db.getUserByUsername(USER_NAME);
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException e) {
            fail(e);
        }
        user = new User(UUID.randomUUID(), USER_NAME, "password", LocalDateTime.of(2020, 1, 1, 0, 0));
        logs = new DBLog[5];
        Category category1 = new Category(UUID.randomUUID(), "Some Category", user.getUserID(), null);
        Category category2 = new Category(UUID.randomUUID(), "Test Category", user.getUserID(), null);
        Cycle cycle = new Cycle(UUID.randomUUID(), "My Cycle", user.getUserID(), category1.getId(), 0, CycleFrequency.fromBinaryString("10000001"));
        logs[0] = new DBLog<>(UUID.randomUUID(), user.getUserID(), category1, DBLog.ChangeMode.INSERT, LocalDateTime.of(2022, 4, 16, 8, 10, 40, 12549));
        logs[1] = new DBLog<>(UUID.randomUUID(), user.getUserID(), category2, DBLog.ChangeMode.INSERT, LocalDateTime.of(2022, 4, 12, 10, 42, 53, 32648));
        logs[2] = new DBLog<>(UUID.randomUUID(), user.getUserID(), cycle, DBLog.ChangeMode.INSERT, LocalDateTime.of(2022, 4, 12, 16, 10, 23, 32395));
        logs[3] = new DBLog<>(UUID.randomUUID(), user.getUserID(), category2, DBLog.ChangeMode.DELETE, LocalDateTime.of(2022, 4, 12, 10, 48, 53, 32648));
        cycle.setArchived(true);
        logs[4] = new DBLog<>(UUID.randomUUID(), user.getUserID(), cycle, DBLog.ChangeMode.UPDATE, LocalDateTime.of(2022, 4, 12, 10, 48, 53, 32648));
        db.insert(user);
    }

    private void tearDown() {
        Log.logToConsole(false);
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException e) {
            fail(e);
        }
    }

    @Test
    public void testInsert() {
        setUp();

        assertEquals(0, dao.getLogs(user).size());
        db.insertLog(logs[0], LocalDateTime.now(App.SERVER_TIMEZONE));
        assertEquals(1, dao.getLogs(user).size());
        db.insertLog(logs[0], LocalDateTime.now(App.SERVER_TIMEZONE));
        assertEquals(1, dao.getLogs(user).size());
        db.insertLog(logs[1], LocalDateTime.now(App.SERVER_TIMEZONE));
        db.insertLog(logs[2], LocalDateTime.now(App.SERVER_TIMEZONE));
        db.insertLog(logs[3], LocalDateTime.now(App.SERVER_TIMEZONE));
        db.insertLog(logs[4], LocalDateTime.now(App.SERVER_TIMEZONE));
        assertEquals(5, dao.getLogs(user).size());

        tearDown();
    }

    @Test
    public void getLogsBetweenTimestamps() {
        setUp();
        db.insertAll(logs);

        assertGetLogsWorks(
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 12, 31, 23, 59, 59)
        );

        assertGetLogsWorks(
                LocalDateTime.of(2022, 4, 12, 10, 42, 53, 32648),
                LocalDateTime.of(2022, 4, 16, 8, 10, 40, 12549)
        );

        assertGetLogsWorks(
                LocalDateTime.of(2022, 4, 12, 10, 45, 29, 9821853),
                LocalDateTime.of(2022, 4, 14, 23, 4, 0, 3690294)
        );



        tearDown();
    }

    private void assertGetLogsWorks(LocalDateTime timestamp1, LocalDateTime timestamp2) {
        assertArrayEquals(
                Arrays.stream(logs)
                        .filter(log -> log.getTimestamp().isAfter(timestamp1) || log.getTimestamp().equals(timestamp1))
                        .filter(log -> log.getTimestamp().isBefore(timestamp2))
                        .sorted()
                        .toArray(DBLog[]::new),
                dao.getLogsBetweenTimestamps(user, timestamp1, timestamp2).toArray(DBLog[]::new)
        );

    }
}