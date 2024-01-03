package de.htw_berlin.communication.protocols;

import de.htw_berlin.communication.pdus.PDU;
import de.htw_berlin.communication.pdus.sync.*;
import de.htw_berlin.engines.models.DBLog;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of interface {@link SyncProtocol}.<br>
 * <code>
 * PROTOCOL_NAME = "COL_SyncProt";<br>
 * VERSION = "v1.0";<br><br>
 * </code>
 * Follows the singleton pattern.
 */
public class SyncProtocolEngine implements SyncProtocol {

    public static String PROTOCOL_NAME = "COL_SyncProt";
    public static String VERSION = "v1.0";

    @Override
    public void run(Socket socket) {
        try {
            // TODO: 03.01.2024 Sync Protocol
            throw new IOException("Sync not implemented yet");
        } catch (NullPointerException | IOException ignored) {
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
