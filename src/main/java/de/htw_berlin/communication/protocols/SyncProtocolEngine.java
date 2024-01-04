package de.htw_berlin.communication.protocols;

import de.htw_berlin.communication.pdus.sync.*;
import de.htw_berlin.engines.models.DBLog;
import de.htw_berlin.logging.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Implementation of SyncProtocol.<br>
 * <code>
 * PROTOCOL_NAME = "COL_SyncProt";<br>
 * VERSION = "v1.0";<br><br>
 * </code>
 * Follows the singleton pattern.
 */
public class SyncProtocolEngine implements Protocol {
    private static final String TAG = SyncProtocolEngine.class.getSimpleName();

    public static String PROTOCOL_NAME = "COL_SyncProt";
    public static String VERSION = "v1.0";

    public static boolean supportsVersion(String version) {
        return VERSION.equals(version);
    }

    @Override
    public void run(Socket socket) throws IOException {
        String client = "Client '" + socket.getRemoteSocketAddress().toString() + "' - ";
        try {
            ProtocolSerializer serializer = new ProtocolSerializer(this, socket);

            // Step 1:
            SendAuthPDU sendAuthPDU = serializer.deserialize(SendAuthPDU.class);
            Log.d(TAG, client + "Received sendAuthPDU: " + sendAuthPDU.getUser());
            // TODO: 04.01.2024 Check Auth in Database
            boolean authSuccessful = true;
            Log.d(TAG, client + "authenticationSuccessful: " + authSuccessful);

            // Step 2:
            if (authSuccessful) {
                serializer.serialize(new AuthVerifiedPDU());
            } else {
                serializer.serialize(new AuthNotVerifiedPDU());
                throw new IOException("Client not authenticated");
            }

            // Step 3:
            SendLogsPDU logsPDU = serializer.deserialize(SendLogsPDU.class);
            DBLog<?>[] logs = logsPDU.getLogs();
            Log.d(TAG, client + "received " + logs.length + " logs from client");
            // TODO: 04.01.2024 Sync DB with logs

            // Step 4:
            DBLog<?>[] instructions = new DBLog[]{}; // TODO: 04.01.2024 Get instructions
            Log.d(TAG, client + "sending " + instructions.length + " instructions to client");
            SendLogsPDU sendLogsPDU = new SendLogsPDU(instructions);
            serializer.serialize(sendLogsPDU);

            // Step 5:
            serializer.deserialize(SyncSuccessfulPDU.class);
            Log.i(TAG, client + "Synchronisation Successful!");
        } catch (ClassCastException e) {
            throw new IOException("Serializing PDU failed");
        }
    }

    @Override
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
