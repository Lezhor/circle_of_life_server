package de.htw_berlin.communication.protocols;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.pdus.PDU;
import de.htw_berlin.communication.pdus.auth.LoginFailedPDU;
import de.htw_berlin.communication.pdus.auth.SendLoginAuthDataPDU;
import de.htw_berlin.communication.pdus.auth.SendUserPDU;
import de.htw_berlin.database.models.User;
import de.htw_berlin.logging.Log;

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
        String client = "Client '" + socket.getRemoteSocketAddress().toString() + "' - ";

        ProtocolSerializer serializer = new ProtocolSerializer(this, socket);

        // Step 1:
        SendLoginAuthDataPDU sendLoginAuthDataPDU = serializer.deserialize(SendLoginAuthDataPDU.class);
        Log.d(TAG, client + "received login request with user " + sendLoginAuthDataPDU.getUsername());

        User authenticatedUser = App.getAuthenticationEngine().getAuthenticatedUser(sendLoginAuthDataPDU.getUsername(), sendLoginAuthDataPDU.getPassword());

        // Step 2:
        PDU answer;
        if (authenticatedUser != null) {
            Log.d(TAG, client + "User authenticated: " + authenticatedUser);
            answer = new SendUserPDU(authenticatedUser);
        } else {
            Log.d(TAG, client + "User NOT authenticated!");
            answer = new LoginFailedPDU();
        }
        serializer.serialize(answer);
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
