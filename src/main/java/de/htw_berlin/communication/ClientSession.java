package de.htw_berlin.communication;

import java.net.Socket;

/**
 * Here the PDUs are received and answered.
 */
public class ClientSession {

    private final Socket clientSocket;

    /**
     * Constructor
     * @param clientSocket clientSocket
     * @throws NullPointerException if passed socket is null
     */
    public ClientSession(Socket clientSocket) throws NullPointerException {
        if (clientSocket == null) {
            throw new NullPointerException("Socket is null");
        }
        this.clientSocket = clientSocket;
    }

    public void start() {

    }

}
