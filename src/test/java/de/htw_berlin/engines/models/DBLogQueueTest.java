package de.htw_berlin.engines.models;

import de.htw_berlin.application.App;
import de.htw_berlin.database.models.*;
import de.htw_berlin.database.models.additional.CycleFrequency;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DBLogQueueTest {

    DBLogQueue queue;
    User user;
    Category category1;
    Category category2;
    Cycle cycle1;
    Todo todo1;

    private void setUp() {
        user = new User(UUID.randomUUID(), "temp_user", "super_cool_password", LocalDateTime.of(2021, 11, 20, 21, 12, 53, 23532));
        category1 = new Category(UUID.randomUUID(), "My Category", user.getUserID(), null);
        category2 = new Category(UUID.randomUUID(), "Another Category", user.getUserID(), null);
        cycle1 = new Cycle(UUID.randomUUID(), "My Cycle", user.getUserID(), category1.getId(), 0, CycleFrequency.fromBinaryString("11101100"));
        todo1 = new Todo(UUID.randomUUID(), "My Todo", user.getUserID(), category1.getId(), 0, false, LocalDateTime.of(2022, 2, 12, 12, 0));

        List<DBLog<?>> serverLogs = new LinkedList<>();
        List<DBLog<?>> clientLogs = new LinkedList<>();

        serverLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), cycle1, DBLog.ChangeMode.INSERT, LocalDateTime.of(2021, 11, 20, 23, 43, 21, 3269)));
        serverLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), category1, DBLog.ChangeMode.INSERT, LocalDateTime.of(2021, 11, 20, 21, 15, 23, 83291)));

        clientLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), category2, DBLog.ChangeMode.INSERT, LocalDateTime.of(2021, 11, 20, 21, 19, 26, 43620)));
        clientLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), todo1, DBLog.ChangeMode.INSERT, LocalDateTime.of(2021, 11, 20, 22, 3, 52, 9320)));
        todo1.setDone(true);
        clientLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), todo1, DBLog.ChangeMode.UPDATE, LocalDateTime.of(2021, 11, 20, 23, 52, 35, 39240)));
        category2.setParentID(category1.getId());
        clientLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), category2, DBLog.ChangeMode.UPDATE, LocalDateTime.of(2022, 1, 20, 9, 3, 6, 3295)));

        queue = new DBLogQueue(serverLogs, clientLogs);
    }

    @Test
    public void testEqualTimestampServerFirst() {
        setUp();
        LocalDateTime timestamp = LocalDateTime.now(App.SERVER_TIMEZONE);
        List<DBLog<?>> serverLogs = new LinkedList<>();
        List<DBLog<?>> clientLogs = new LinkedList<>();
        serverLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), category1, DBLog.ChangeMode.INSERT, timestamp));
        clientLogs.add(new DBLog<>(UUID.randomUUID(), user.getUserID(), category2, DBLog.ChangeMode.INSERT, timestamp));

        queue = new DBLogQueue(serverLogs, clientLogs);

        assertTrue(queue.hasNext());
        assertFalse(queue.isNextClient());
        assertEquals(serverLogs.get(0), queue.poll());
        assertEquals(clientLogs.get(0), queue.poll());
        assertFalse(queue.hasNext());

        queue = new DBLogQueue(serverLogs, clientLogs);
        Iterator<DBLog<?>> iterator = queue.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(serverLogs.get(0), iterator.next());
        assertEquals(clientLogs.get(0), iterator.next());
        assertFalse(iterator.hasNext());

    }

    @Test
    public void testCorrectOrder() {
        setUp();
        assertTrue(queue.hasNext());
        assertFalse(queue.isNextClient());
        assertEquals(category1, queue.poll().getChangedObject());
        assertTrue(queue.isNextClient());
        assertEquals(category2, queue.poll().getChangedObject());
        assertEquals(todo1, queue.poll().getChangedObject());
        assertEquals(cycle1, queue.poll().getChangedObject());
        assertEquals(todo1, queue.poll().getChangedObject());
        assertEquals(category2, queue.poll().getChangedObject());
        assertFalse(queue.hasNext());
    }

}