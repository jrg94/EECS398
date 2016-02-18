package eecs398_lock;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by JRG94 on 2/18/2016.
 */
public class BluetoothLockService {

    // FIELDS //

    // Name when creating socket
    private static final String NAME = "i BT";

    // UUID for this application
    private static UUID appUUID;

    // Member Fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private int mState;

    private ArrayList<UUID> mUUIDs;

    // CONSTANTS //

    public static final int STATE_NONE = 0;

    // CONSTRUCTORS //

    public BluetoothLockService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
        mState = STATE_NONE;
    }

    // GETTER/SETTERS //

    public static UUID getAppUUID() {
        return appUUID;
    }

    public static void setAppUUID(UUID appUUID) {
        BluetoothLockService.appUUID = appUUID;
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            BluetoothServerSocket temp = null;

            // Try creating a server socket
            try {
                // If bluetooth is on
                if (mAdapter.isEnabled()) {
                    BluetoothLockService.setAppUUID(UUID.fromString("00001101-0000-1000-8000-"
                                                    + mAdapter.getAddress().replace(":", "")));
                }
                // TODO: Log UUID
                temp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, BluetoothLockService.getAppUUID());
            }
            catch (IOException e) {
                // TODO: Listen failed
            }
            mServerSocket = temp;
        }
    }
}
