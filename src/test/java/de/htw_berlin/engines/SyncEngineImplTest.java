package de.htw_berlin.engines;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.pdus.sync.SendLogsPDU;
import de.htw_berlin.database.control.DatabaseController;
import de.htw_berlin.database.models.*;
import de.htw_berlin.database.models.additional.CycleFrequency;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.logging.Log;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SyncEngineImplTest {

    // TODO: 06.01.2024 test syncing!

    DatabaseController db = App.getDatabaseController();
    private User user;
    public static final String USER_NAME = "another_completely_random_name_so_that_no_one_picks_it_31432";
    Category category1, category2, category3;
    Cycle cycle;
    Todo todo;

    private void setUp() {
        Log.logToConsole(true);
        user = db.getUserByUsername(USER_NAME);
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException e) {
            fail(e);
        }
        user = new User(UUID.randomUUID(), USER_NAME, "password", LocalDateTime.of(2019, 12, 30, 12, 5, 36, 213456436));
        db.insert(user);
        category1 = new Category(UUID.randomUUID(), "Category 1", user.getUserID(), null);
        category2 = new Category(UUID.randomUUID(), "Category 2", user.getUserID(), null);
        category3 = new Category(UUID.randomUUID(), "Category 3", user.getUserID(), null);
        cycle = new Cycle(UUID.randomUUID(), "Cycle 1", user.getUserID(), category1.getId(), 0, CycleFrequency.fromBinaryString("10000001"), false);
        todo = new Todo(UUID.randomUUID(), "Todo 1", user.getUserID(), category2.getId(), 1, true, LocalDateTime.now());
    }

    private void tearDown() {
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException ignored) {
        }
        Log.logToConsole(false);
    }

    @Test
    public void testSync() {
        setUp();

        Category category1Alter = new Category(category1);
        category1Alter.setName("Other Category 1");
        DBLog<?>[] logs = generateLogs(user,
                new DBLogBuilder<>(category1, DBLog.ChangeMode.INSERT),
                new DBLogBuilder<>(category2, DBLog.ChangeMode.INSERT),
                new DBLogBuilder<>(category1Alter, DBLog.ChangeMode.UPDATE),
                new DBLogBuilder<>(todo, DBLog.ChangeMode.INSERT),
                new DBLogBuilder<>(cycle, DBLog.ChangeMode.INSERT)
        );

        DBLog<?>[] serverLogs = getServerLogs(logs[0], logs[2], logs[4]);
        DBLog<?>[] clientLogs = getClientLogs(logs[1], logs[3]);

        SendLogsPDU clientPDU = getClientPDU(clientLogs);

        SendLogsPDU returnedPDU = App.getSyncEngine().sync(user, clientPDU);

        assertArrayEquals(returnedPDU.getLogs(), serverLogs);
        // TODO: 06.01.2024 Assert stuff with database

        tearDown();
    }

    private DBLog<?>[] getServerLogs(DBLog<?>... logs) {
        List<DBLog<?>> out = new LinkedList<>();
        for (DBLog<?> log : logs) {
            if (db.executeLog(log)) {
                db.insertLog(log);
            }
            out.add(log);
        }
        return out.toArray(DBLog[]::new);
    }

    private DBLog<?>[] getClientLogs(DBLog<?>... logs) {
        return logs;
    }

    private SendLogsPDU getClientPDU(DBLog<?>... logs) {
        return new SendLogsPDU(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0), logs);
    }

    private DBLog<?>[] generateLogs(User user, DBLogBuilder<?>... builders) {
        Collection<DBLog<?>> logs = new LinkedList<>();
        for (int i = 0; i < builders.length; i++) {
            logs.add(builders[i].setUser(user).setPriority(i).build());
        }
        return logs.toArray(DBLog[]::new);
    }

    private static class DBLogBuilder<E extends Entity> {

        private User user;
        private E changedObject;
        private DBLog.ChangeMode changeMode;
        private int priority;

        public DBLogBuilder(E changedObject, DBLog.ChangeMode changeMode) {
            this.user = null;
            this.changedObject = changedObject;
            this.changeMode = changeMode;
            this.priority = 0;
        }

        public E getChangedObject() {
            return changedObject;
        }

        public DBLogBuilder<E> setChangedObject(E changedObject) {
            this.changedObject = changedObject;
            return this;
        }

        public DBLog.ChangeMode getChangeMode() {
            return changeMode;
        }

        public DBLogBuilder<E> setChangeMode(DBLog.ChangeMode changeMode) {
            this.changeMode = changeMode;
            return this;
        }

        public User getUser() {
            return user;
        }

        public DBLogBuilder<E> setUser(User user) {
            this.user = user;
            return this;
        }

        /**
         * a lower value here means it came before one with a higher value.
         *
         * @param priority priority
         * @return self
         */
        public DBLogBuilder<E> setPriority(int priority) {
            this.priority = priority + 1;
            return this;
        }

        public DBLog<E> build() {
            return new DBLog<>(UUID.randomUUID(), user.getUserID(), changedObject, changeMode, LocalDateTime.of(2020, 1, 1, 0, 0, 0, priority));
        }

    }

}