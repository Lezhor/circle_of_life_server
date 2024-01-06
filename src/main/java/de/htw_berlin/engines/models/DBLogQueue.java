package de.htw_berlin.engines.models;

import java.util.*;
import java.util.function.Function;

/**
 * Double-Queue data-structure. Saves server- and client-logs. peek() and poll() return next value based on
 */
public class DBLogQueue implements Iterable<DBLog<?>> {

    private final Queue<DBLog<?>> serverLogs;
    private final Queue<DBLog<?>> clientLogs;

    /**
     * Constructor for queue
     * @param serverLogs serverLogs
     * @param clientLogs clientLogs
     */
    public DBLogQueue(Collection<DBLog<?>> serverLogs, Collection<DBLog<?>> clientLogs) {
        this.serverLogs = new LinkedList<>(serverLogs.stream().sorted(Comparator.comparing(DBLog::getTimestamp)).toList());
        this.clientLogs = new LinkedList<>(clientLogs.stream().sorted(Comparator.comparing(DBLog::getTimestamp)).toList());
    }

    /**
     * Constructor for cloning queue
     * @param that queue to be cloned
     */
    public DBLogQueue(DBLogQueue that) {
        this(that.serverLogs, that.clientLogs);
    }

    /**
     * Checks if next upcoming log comes from client
     * @return true {@link #peek()} log comes from client
     */
    public boolean isNextClient() {
        if (clientLogs.isEmpty()) {
            return false;
        } else if (serverLogs.isEmpty()) {
            return true;
        } else {
            //noinspection DataFlowIssue
            return serverLogs.peek().getTimestamp().isAfter(clientLogs.peek().getTimestamp());
        }
    }

    /**
     * returns next log based on timestamp order but doesn't delete it from the list.
     * @return next log
     */
    public DBLog<?> peek() {
        return getNext(Queue::peek);
    }

    /**
     * returns next log based on timestamp order and deletes it from the list.
     * @return next log
     */
    public DBLog<?> poll() {
        return getNext(Queue::poll);
    }

    /**
     * Gets upcoming value using specified getter
     * @param getMethod getter
     * @return upcoming value
     */
    private DBLog<?> getNext(Function<Queue<DBLog<?>>, DBLog<?>> getMethod) {
        if (serverLogs.isEmpty() && clientLogs.isEmpty()) {
            return null;
        } else if (serverLogs.isEmpty()) {
            return getMethod.apply(clientLogs);
        } else if (clientLogs.isEmpty()) {
            return getMethod.apply(serverLogs);
        } else {
            //noinspection DataFlowIssue
            if (serverLogs.peek().getTimestamp().isAfter(clientLogs.peek().getTimestamp())) {
                return getMethod.apply(clientLogs);
            } else {
                return getMethod.apply(serverLogs);
            }
        }
    }

    /**
     * Checks if queue has more items or not
     * @return true if there are more items to come.
     */
    public boolean hasNext() {
        return peek() != null;
    }

    @Override
    public Iterator<DBLog<?>> iterator() {
        return new Iter(this);
    }

    private static class Iter implements Iterator<DBLog<?>> {

        private final DBLogQueue queue;

        @SuppressWarnings("unused")
        private Iter() {
            this(new DBLogQueue(new LinkedList<>(), new LinkedList<>()));
        }

        private Iter(DBLogQueue queue) {
            this.queue = new DBLogQueue(queue);
        }

        @Override
        public boolean hasNext() {
            return queue.hasNext();
        }

        @Override
        public DBLog<?> next() {
            return queue.poll();
        }
    }
}
