package de.htw_berlin.engines.models;

import de.htw_berlin.database.models.type_converters.DBLogConverter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * LogSerializer can serialize and deserialize {@link DBLog logs} from and to IOStreams.<br>
 * It is used in the {@link de.htw_berlin.communication.pdus.sync.SendLogsPDU SendLogsPDU}.
 */
public class LogSerializer {

    private static volatile LogSerializer instance;

    /**
     * Follows the singleton pattern:<br>
     * Returns the only existing instance of this class
     *
     * @return instance of this class
     */
    public static LogSerializer getInstance() {
        if (instance == null) {
            synchronized (LogSerializer.class) {
                if (instance == null)
                    instance = new LogSerializer();
            }
        }
        return instance;
    }

    private LogSerializer() {
    }

    public void serialize(OutputStream os, DBLog<?> log) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF(DBLogConverter.dbLogToString(log));
    }

    public DBLog<?> deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        return DBLogConverter.stringToDBLog(dis.readUTF());
    }

}
