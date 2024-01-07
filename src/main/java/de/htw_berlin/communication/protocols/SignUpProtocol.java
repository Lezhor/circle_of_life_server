package de.htw_berlin.communication.protocols;

import de.htw_berlin.application.App;
import de.htw_berlin.communication.pdus.PDU;
import de.htw_berlin.communication.pdus.auth.SendLoginAuthDataPDU;
import de.htw_berlin.communication.pdus.auth.SendUserPDU;
import de.htw_berlin.communication.pdus.auth.SignUpFailedPDU;
import de.htw_berlin.database.models.User;
import de.htw_berlin.logging.Log;

import java.io.IOException;
import java.net.Socket;

public class SignUpProtocol implements Protocol {
    private static final String TAG = SignUpProtocol.class.getSimpleName();

    public static String PROTOCOL_NAME = "COL_SignUpProt";
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
        Log.d(TAG, client + "signUp request got received for username: " + sendLoginAuthDataPDU.getUsername());

        User user = App.getAuthenticationEngine().signUp(sendLoginAuthDataPDU.getUsername(), sendLoginAuthDataPDU.getPassword());

        // Step 2:
        PDU answer;
        if (user != null) {
            Log.d(TAG, client + "successfully created user: " + user);
            answer = new SendUserPDU(user);
        } else {
            Log.d(TAG, client + "signing up failed!");
            answer = new SignUpFailedPDU();
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
