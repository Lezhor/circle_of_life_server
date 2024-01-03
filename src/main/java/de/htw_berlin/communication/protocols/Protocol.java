package de.htw_berlin.communication.protocols;

import java.net.Socket;

public interface Protocol {

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
