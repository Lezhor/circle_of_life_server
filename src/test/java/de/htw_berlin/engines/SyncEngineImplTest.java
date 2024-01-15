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
        todo = new Todo(UUID.randomUUID(), "Todo 1", user.getUserID(), category2.getId(), 1, true, LocalDateTime.now(App.SERVER_TIMEZONE));
    }

    private void tearDown() {
        try {
            db.clearUser(user);
        } catch (OperationNotSupportedException ignored) {
        }
        Log.logToConsole(false);
    }

    /**
     * A general SyncCase<br>
     * Creates 5 Logs. second and fourth are client logs, the other 3 are executed on the server before the sync.
     * Than the sync happens: The SyncEngine should return the serverlogs 1,3,5. Also changes from the client-logs should be applied to the database.
     */
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

        // Setup Server
        DBLog<?>[] serverLogs = getServerLogs(logs[0].getTimestamp(), logs[0], logs[2], logs[4]);
        DBLog<?>[] clientLogs = getClientLogs(logs[1], logs[3]);

        SendLogsPDU clientPDU = getClientPDU(clientLogs);

        // Asserts before sync
        assertTrue(db.exists(category1));
        assertFalse(db.exists(category2));
        assertTrue(db.exists(cycle));
        assertFalse(db.exists(todo));


        SendLogsPDU returnedPDU = App.getSyncEngine().sync(user, clientPDU);

        // Asserts after sync
        assertTrue(db.exists(category1));
        assertTrue(db.exists(category2));
        assertTrue(db.exists(cycle));
        assertTrue(db.exists(todo));

        assertArrayEquals(returnedPDU.getLogs(), serverLogs);
        // TODO: 06.01.2024 Assert stuff with database

        tearDown();
    }

    @Test
    public void testSyncUpdateClientOverrides() {
        setUp();

        db.insert(category1);

        Category clientsVersion = category1.copy();
        clientsVersion.setName("Clients Category");
        Category serverVersion = category1.copy();
        serverVersion.setName("Servers Category");

        DBLog<?>[] logs = generateLogs(user,
                new DBLogBuilder<>(serverVersion, DBLog.ChangeMode.UPDATE),
                new DBLogBuilder<>(clientsVersion, DBLog.ChangeMode.UPDATE)
                );

        DBLog<?>[] serverLogs = getServerLogs(logs[0].getTimestamp(), logs[0]);
        DBLog<?>[] clientLogs = getClientLogs(logs[1]);

        // Asserts before sync
        assertTrue(serverVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));
        assertFalse(clientsVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));

        SendLogsPDU receivedPDU = App.getSyncEngine().sync(user, getClientPDU(clientLogs));

        // Assert no changes for client
        assertEquals(0, receivedPDU.getLogs().length);

        // Asserts after sync
        assertFalse(serverVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));
        assertTrue(clientsVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));

        tearDown();
    }

    @Test
    public void testSyncUpdateServerOverrides() {
        setUp();

        db.insert(category1);

        Category clientsVersion = category1.copy();
        clientsVersion.setName("Clients Category");
        Category serverVersion = category1.copy();
        serverVersion.setName("Servers Category");

        DBLog<?>[] logs = generateLogs(user,
                new DBLogBuilder<>(clientsVersion, DBLog.ChangeMode.UPDATE),
                new DBLogBuilder<>(serverVersion, DBLog.ChangeMode.UPDATE)
        );

        DBLog<?>[] serverLogs = getServerLogs(logs[1].getTimestamp(), logs[1]);
        DBLog<?>[] clientLogs = getClientLogs(logs[0]);

        // Asserts before sync
        assertTrue(serverVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));
        assertFalse(clientsVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));

        SendLogsPDU receivedPDU = App.getSyncEngine().sync(user, getClientPDU(clientLogs));

        // Assert no changes for client
        assertArrayEquals(serverLogs, receivedPDU.getLogs());

        // Asserts after sync
        assertTrue(serverVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));
        assertFalse(clientsVersion.equalsAllParams(db.getById(category1.getId(), Category.class)));

        tearDown();
    }

    /**
     * Test following scenario:<br>
     * Suppose there is an Entity E in the database.<br>
     * <pre>
     *     Client A updates E at timestamp 1
     *     Client B updates E at timestamp 2
     *     Client A syncs at timestamp 3
     *     Client B syncs at timestamp 4
     * </pre>
     * Now if Client A would sync again it would NOT be able to fetch the most recent changes made by B,
     * because these happened at timestamp 2 and A already has synced everything until timestamp 3
     */
    @Test
    public void testSync3ClientsProblem() {
        setUp();
        db.insert(category1);
        Category c2 = category1.copy();
        c2.setName("Variant 2");
        Category c3 = category1.copy();
        c3.setName("Variant 3");

        DBLog<Category> log1 = new DBLog<>(UUID.randomUUID(), user.getUserID(), c2, DBLog.ChangeMode.UPDATE, LocalDateTime.of(2021, 1, 1, 0, 0));
        DBLog<Category> log2 = new DBLog<>(UUID.randomUUID(), user.getUserID(), c3, DBLog.ChangeMode.UPDATE, LocalDateTime.of(2021, 2, 1, 0, 0));

        // assert db did not change
        Category actual1 = db.getById(category1.getId(), Category.class);
        assertFalse(c2.equalsAllParams(actual1));
        assertFalse(c3.equalsAllParams(actual1));

        SendLogsPDU pduA = new SendLogsPDU(LocalDateTime.of(2020, 1, 1, 0, 0), log1);
        SendLogsPDU pduARet = App.getSyncEngine().sync(user, pduA);

        // assert db first change
         Category actual2 = db.getById(category1.getId(), Category.class);
        assertTrue(c2.equalsAllParams(actual2));
        assertFalse(c3.equalsAllParams(actual2));
        assertEquals(0, pduARet.getLogs().length);

        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }

        SendLogsPDU pduB = new SendLogsPDU(LocalDateTime.of(2020, 1, 1, 0, 0), log2);
        SendLogsPDU pduBRet = App.getSyncEngine().sync(user, pduB);

        // assert db second change
        Category actual3 = db.getById(category1.getId(), Category.class);
        assertFalse(c2.equalsAllParams(actual3));
        assertTrue(c3.equalsAllParams(actual3));
        assertEquals(0, pduBRet.getLogs().length);

        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }

        SendLogsPDU pduA2 = new SendLogsPDU(pduARet.getLastSyncDate());
        SendLogsPDU pduA2Ret = App.getSyncEngine().sync(user, pduA2);

        // assert db after last sync
        Category actual4 = db.getById(category1.getId(), Category.class);
        assertFalse(c2.equalsAllParams(actual4));
        assertTrue(c3.equalsAllParams(actual4));
        assertEquals(1, pduA2Ret.getLogs().length);
        assertEquals(log2, pduA2Ret.getLogs()[0]);

        tearDown();
    }

    private DBLog<?>[] getServerLogs(LocalDateTime insertTimestamp, DBLog<?>... logs) {
        List<DBLog<?>> out = new LinkedList<>();
        for (DBLog<?> log : logs) {
            if (db.executeLog(log)) {
                db.insertLog(log, insertTimestamp);
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