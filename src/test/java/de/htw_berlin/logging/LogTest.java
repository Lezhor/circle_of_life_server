package de.htw_berlin.logging;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LogTest {
    public static final String TAG = LogTest.class.getSimpleName();

    @Test
    public void testLog() throws InterruptedException {
        Log.logToConsole(true);
        Log.d(TAG, "Hello There");
        Thread.sleep(236);
        Log.i(TAG, "This is a test log, actually nothing happened lol");
        Thread.sleep(32);
        Log.d(TAG, "Whatever");
        Thread.sleep(632);
        Log.w(TAG, "And now some exception occurred!", new IOException("Serializing failed"));
    }

}