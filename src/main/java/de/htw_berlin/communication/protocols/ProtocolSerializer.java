package de.htw_berlin.communication.protocols;

import de.htw_berlin.communication.pdus.PDU;
import de.htw_berlin.communication.pdus.PDUSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Can be used by a protocol to serializes pdus from and to IO-Streams
 */
public class ProtocolSerializer {

    private final Protocol protocol;
    private final PDUSerializer pduSerializer;
    private final InputStream is;
    private final OutputStream os;


    /**
     * Constructor for the serializer. Also sends the Header to OutputStream
     * @param protocol      protocol in order to get the protocol name and version
     * @param socket client socket.
     */
    public ProtocolSerializer(Protocol protocol, Socket socket) throws NullPointerException {
        this.protocol = protocol;
        try {
            this.is = Objects.requireNonNull(socket.getInputStream());
            this.os = Objects.requireNonNull(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pduSerializer = new PDUSerializer(socket);
    }

    public void serialize(PDU pdu) throws IOException {
        serializeHeader();
        pdu.serialize(os);
    }

    /**
     * Deserializes PDU from Inputstream.<br>
     * Deserilizes the PDU ID and than calls the {@link PDU#deserialize(InputStream) deserialize()} method on specific PDU
     * @return returns deserialized PDU
     * @throws IOException if deseralizing fails
     */
    public PDU deserialize() throws IOException {
        deserializeHeader();
        return pduSerializer.deserialize();
    }

    /**
     * Deserialized PDU from InputStream by calling {@link ProtocolSerializer#deserialize()}.
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

    /**
     * Serializes Protocol header to OutputStream.<br>
     * Writes String {@link Protocol#getProtocolName() protocolName} followed by String {@link Protocol#getVersion() version}
     *
     * @throws IOException if serializing fails
     */
    private void serializeHeader() throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF(protocol.getProtocolName());
        dos.writeUTF(protocol.getVersion());
    }

    /**
     * Deserializes Header from InputStream. If no Exception thrown the Header is valid.
     *
     * @throws IOException if reading failed or if potocol name or version are not like in the specified
     */
    private void deserializeHeader() throws IOException {
        DataInputStream dis = new DataInputStream(is);
        String protocolName;
        String version;
        try {
            protocolName = dis.readUTF();
            version = dis.readUTF();
        } catch (IOException e) {
            throw new IOException("Failed deserializing header");
        }
        if (!protocolName.equals(protocol.getProtocolName())) {
            throw new IOException("Protocol Name mismatch! Expected: '" + protocol.getProtocolName() + "', Found: '" + protocolName + "'");
        }
        if (!version.equals(protocol.getVersion())) {
            throw new IOException("Protocol Version mismatch! Expected: '" + protocol.getVersion() + "', Found: '" + version + "'");
        }
    }

}
