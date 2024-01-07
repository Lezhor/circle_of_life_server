package de.htw_berlin.communication.protocols;

import java.io.IOException;
import java.net.Socket;

/**
 * Protocol interface has methods getProtocolName() getVersion() and run()<br>
 * the run() method is the actual implementation of the protocol.<br>
 * The other two are needed for the header of the protocol e.g. in the ProtocolSerializer
 * @see ProtocolSerializer
 */
public interface Protocol {

    /**
     * Returns the protocol implementation which matches the given name and version.
     * @param name name of the protocol
     * @param version version of the protocol
     * @return protocol instance or null if no matching protocol is found
     */
    static Protocol get(String name, String version) {
        if (name.equals(SyncProtocolEngine.PROTOCOL_NAME) && SyncProtocolEngine.supportsVersion(version)) {
            return new SyncProtocolEngine();
        } else if (name.equals(LoginProtocol.PROTOCOL_NAME) && LoginProtocol.supportsVersion(version)) {
            return new LoginProtocol();
        } else if (name.equals(SignUpProtocol.PROTOCOL_NAME) && SignUpProtocol.supportsVersion(version)) {
            return new SignUpProtocol();
        } else {
            return null;
        }
    }

    /**
     * Implementation of the protocol, with serializing and deserializing pdu
     * @param socket clientSocket
     * @throws IOException if running protocol goes wrong. e.g. deserializing PDU
     */
    void run(Socket socket) throws IOException;

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
