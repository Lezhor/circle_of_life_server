package de.htw_berlin.communication.protocols;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.pdus.sync.*;
import de.htw_berlin.database.models.type_converters.LocalDateTimeConverter;
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
            boolean authSuccessful = App.getAuthenticationEngine().isAuthenticated(sendAuthPDU.getUser());
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
            Log.d(TAG, client + "received " + logsPDU.getLogs().length + " logs from client. LastSyncDate: " + LocalDateTimeConverter.localDateTimeToString(logsPDU.getLastSyncDate()));


            // Step 4:
            SendLogsPDU sendLogsPDU = App.getSyncEngine().sync(sendAuthPDU.getUser(), logsPDU);
            Log.d(TAG, client + "sending " + sendLogsPDU.getLogs().length + " instructions to client");
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
