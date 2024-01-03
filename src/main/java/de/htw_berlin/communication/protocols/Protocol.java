package de.htw_berlin.communication.protocols;

import java.net.Socket;

public interface Protocol {

    static Protocol get(String name, String version) {
        if (name.equals(SyncProtocolEngine.PROTOCOL_NAME) && SyncProtocolEngine.supportsVersion(version)) {
            return new SyncProtocolEngine();
        } else {
            return null;
        }
    }

    void run(Socket socket);

    /**
     * Returns the current Protocol Name
     * @return protocol name
     */
    String getProtocolName();

    /**
     * Get current version of the syncProtocol
     * @return version of this protocol
     */
    String getVersion();

}
