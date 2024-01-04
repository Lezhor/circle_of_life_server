package de.htw_berlin.communication.protocols;

import de.htw_berlin.communication.pdus.sync.*;
import de.htw_berlin.engines.models.DBLog;

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

    public static String PROTOCOL_NAME = "COL_SyncProt";
    public static String VERSION = "v1.0";

    public static boolean supportsVersion(String version) {
        return VERSION.equals(version);
    }

    @Override
    public void run(Socket socket) throws IOException {
        try {
            ProtocolSerializer serializer = new ProtocolSerializer(this, socket);

            // Step 1:
            SendAuthPDU sendAuthPDU = serializer.deserialize(SendAuthPDU.class);
            // TODO: 04.01.2024 Check Auth in Database
            boolean authSuccessful = true;

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
            // TODO: 04.01.2024 Sync DB with logs

            // Step 4:
            DBLog<?>[] instructions = new DBLog[]{}; // TODO: 04.01.2024 Get instructions
            SendLogsPDU sendLogsPDU = new SendLogsPDU(instructions);
            serializer.serialize(sendLogsPDU);

            // Step 5:
            serializer.deserialize(SyncSuccessfulPDU.class);
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
