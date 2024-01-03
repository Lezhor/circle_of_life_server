package de.htw_berlin.communication.pdus.auth;

import de.htw_berlin.communication.pdus.PDUWithNoData;

import java.io.IOException;
import java.io.InputStream;

/**
 * Part of the SignUpProtocol.
 * Server sends this to client if signUp succeeded
 */
public class SignUpSucceededPDU implements PDUWithNoData {

    public static final int ID = 105;

    @Override
    public int getID() {
        return ID;
    }

    /**
     * Deserializes pdu by calling {@link #deserialize(InputStream)}
     * @param is input stream
     * @return instance of this class
     * @throws IOException if deserializing fails
     */
    public static SignUpSucceededPDU fromInputStream(InputStream is) throws IOException {
        SignUpSucceededPDU pdu = new SignUpSucceededPDU();
        pdu.deserialize(is);
        return pdu;
    }
}
