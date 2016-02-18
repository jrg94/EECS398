package eecs398_lock;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;

/**
 * Created by JRG94 on 2/18/2016.
 */
public class BluetoothLockService {

    // FIELDS //
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private int mState;

    // CONSTANTS //
    public static final int STATE_NONE = 0;

    // CONSTRUCTORS //
    public BluetoothLockService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
        mState = STATE_NONE;
    }


}
