package de.htw_berlin.communication;

import de.htw_berlin.communication.protocols.Protocol;
import de.htw_berlin.logging.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Here the PDUs are received and answered.
 */
public class ClientSession {
    private static final String TAG = ClientSession.class.getSimpleName();

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

    public void start() throws IOException {
        Log.d(TAG, "Starting Client Session");
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        String protocolName = dis.readUTF();
        String protocolVersion = dis.readUTF();
        Protocol protocol = Protocol.get(protocolName, protocolVersion);
        if (protocol != null) {
            Log.d(TAG, "Starting Protocol: " + protocol.getProtocolName() + "-" + protocol.getVersion());
            protocol.run(clientSocket);
        }
    }

}
