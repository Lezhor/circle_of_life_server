package de.htw_berlin.communication.pdus;

import de.htw_berlin.communication.pdus.auth.*;
import de.htw_berlin.communication.pdus.sync.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Can be used by a protocol to serializes pdus from and to IO-Streams
 */
public class PDUSerializer {

    private final InputStream is;
    private final OutputStream os;


    /**
     * Constructor for the serializer
     *
     * @param communication instance of socket Communication.
     */
    public PDUSerializer(Socket communication) throws NullPointerException {
        try {
            this.is = Objects.requireNonNull(communication.getInputStream());
            this.os = Objects.requireNonNull(communication.getOutputStream());
        } catch (IOException e) {
            throw new NullPointerException("Cannot retrieve streams from SocketCommunication because not connected to server yet");
        }
    }

    public void serialize(PDU pdu) throws IOException {
        pdu.serialize(os);
    }

    /**
     * Deserializes PDU from Inputstream.<br>
     * Deserilizes the PDU ID and than calls the {@link PDU#deserialize(InputStream) deserialize()} method on specific PDU
     * @return returns deserialized PDU
     * @throws IOException if deseralizing fails
     */
    public PDU deserialize() throws IOException {
        DataInputStream dis = new DataInputStream(is);
        return switch (dis.readInt()) {
            case SendAuthPDU.ID -> SendAuthPDU.fromInputStream(is);
            case AuthVerifiedPDU.ID -> AuthVerifiedPDU.fromInputStream(is);
            case AuthNotVerifiedPDU.ID -> AuthNotVerifiedPDU.fromInputStream(is);
            case SendLogsPDU.ID -> SendLogsPDU.fromInputStream(is);
            case SendInstructionsPDU.ID -> SendInstructionsPDU.fromInputStream(is);
            case SyncSuccessfulPDU.ID -> SyncSuccessfulPDU.fromInputStream(is);
            case SendLoginAuthDataPDU.ID -> SendLoginAuthDataPDU.fromInputStream(is);
            case LoginFailedPDU.ID -> LoginFailedPDU.fromInputStream(is);
            case SendUserPDU.ID -> SendUserPDU.fromInputStream(is);
            default -> throw new IOException("Failed to deserialize PDU");
        };
    }

    /**
     * Deserialized PDU from InputStream by calling {@link PDUSerializer#deserialize()}.
     * Casts result to desired PDU-Type.<br>
     * This fails if passed pdu-class mismatches the pdu deserialized from the InputStream.
     * @param pduClass Class of the desired pdu
     * @return casted PDU
     * @param <P> Type of the PDU
     * @throws IOException if deserializing failed
     * @throws ClassCastException if casting failed
     */
    public <P extends PDU> P deserialize(Class<P> pduClass) throws IOException, ClassCastException {
        PDU deserialized = deserialize();
        return pduClass.cast(deserialized);
    }

}
