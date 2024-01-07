package de.htw_berlin.communication.protocols;

import java.io.IOException;
import java.net.Socket;

public class LoginProtocol implements Protocol {
    private static final String TAG = LoginProtocol.class.getSimpleName();

    public static String PROTOCOL_NAME = "COL_LoginProt";
    public static String VERSION = "v1.0";

    public static boolean supportsVersion(String version) {
        return VERSION.equals(version);
    }

    @Override
    public void run(Socket socket) throws IOException {
        // TODO: 07.01.2024 Login Protocol
        throw new IOException("Protocol not implemented yet");
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
