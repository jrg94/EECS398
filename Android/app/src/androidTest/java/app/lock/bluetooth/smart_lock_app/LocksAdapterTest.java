package app.lock.bluetooth.smart_lock_app;

import android.test.AndroidTestCase;

import java.util.HashMap;

import eecs398_lock.LocksAdapter;
import eecs398_lock.SmartLock;

/**
 * Created by JRG94 on 4/20/2016.
 */
public class LocksAdapterTest extends AndroidTestCase {

    private LocksAdapter mAdapter;

    private static final String testMACAddres = "AA:BB:CC:DD:EE:FF";

    public LocksAdapterTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        HashMap<String, SmartLock> data = new HashMap<String, SmartLock>();

        data.put(testMACAddres, new SmartLock(testMACAddres));

        mAdapter = new LocksAdapter(getContext(), data);
    }

    public void testGetCount() {
        assertEquals("Lock count incorrect", 1, mAdapter.getCount());
    }
}
