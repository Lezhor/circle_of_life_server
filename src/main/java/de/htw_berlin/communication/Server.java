package de.htw_berlin.communication;

import de.htw_berlin.logging.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * This class has a loop which waits for a client to connect and then opens a {@link ClientSession} with this client.
 */
public class Server {
    private static final String TAG = Server.class.getSimpleName();

    /**
     * The PortNumber the server uses for the Sockt
     */
    public static final int PORT = 31163;

    private final Thread serverThread;
    private final ServerSocket serverSocket;
    private final ExecutorService service;

    public Server() {
        Log.d(TAG, "Instantiating new Server");
        serverThread = new Thread(this::serverSession);
        service = Executors.newFixedThreadPool(10);
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            Log.w(TAG, "Instantiating ServerSocket on Port " + PORT + " failed", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the serverThread where the serverSession() runs on.
     */
    public void start() {
        if (!serverSocket.isClosed()) {
            Log.d(TAG, "Starting ServerThread");
            serverThread.start();
        }
    }

    /**
     * interrupts server session.
     */
    public void shutDown() {
        if (!serverSocket.isClosed()) {
            Log.d(TAG, "Shutting Down...");
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
            serverThread.interrupt();
            service.shutdown();
        }
    }

    /**
     * Opens a ServerSocket and waits for users to connect. An accepted connection is transferred to a separate Thread
     */
    private void serverSession() {
        Log.d(TAG, "Starting Server Session");
        Log.d(TAG, "Listening on port " + PORT);
        while (!Thread.interrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();
                Log.i(TAG, "Client Connected!");
                service.submit(() -> onClientConnected(clientSocket));
            } catch (IOException e) {
                if (serverSocket.isClosed()) {
                    break;
                }
            }
        }
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Is executed every time a client connects to the server.
     * @param clientSocket clientSocket
     */
    private void onClientConnected(Socket clientSocket) {
        ClientSession clientSession = new ClientSession(clientSocket);
        try {
            clientSession.start();
        } catch (IOException ignored) {
        } finally {
            if (!clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
