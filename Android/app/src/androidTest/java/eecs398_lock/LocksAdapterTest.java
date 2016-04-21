package eecs398_lock;

import android.test.AndroidTestCase;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;

import app.lock.bluetooth.smart_lock_app.R;
import eecs398_lock.LocksAdapter;
import eecs398_lock.SmartLock;

/**
 * Created by JRG94 on 4/20/2016.
 */
public class LocksAdapterTest extends AndroidTestCase {

    private LocksAdapter mAdapter;
    private HashMap<String, SmartLock> data;

    private static final String testMACAddres = "AA:BB:CC:DD:EE:FF";

    public LocksAdapterTest() {
        super();
    }

    /**
     * Initializes a test adapter
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new HashMap<String, SmartLock>();

        data.put(testMACAddres, new SmartLock(testMACAddres));

        mAdapter = new LocksAdapter(getContext(), data);
    }

    /**
     * Tests that the adapter returns the correct element count
     */
    public void testGetCount() {
        assertEquals("Lock count incorrect", data.size(), mAdapter.getCount());
    }

    /**
    @UiThreadTest
    public void testGetView() {
        View view = mAdapter.getView(0, null, null);

        // Initialize the UI elements
        TextView lockLabel = (TextView) view.findViewById(R.id.lockLabel);
        TextView connectedStatus = (TextView) view.findViewById(R.id.connectedStatus);
        Switch lockStatus = (Switch) view.findViewById(R.id.lockState);
        Button popupMenuButton = (Button) view.findViewById(R.id.popup_lock_menu_button);

        // Test that all views exist
        assertNotNull("View is null", view);
        assertNotNull("LockLabel TextView is null", lockLabel);
        assertNotNull("ConnectedStatus TextView is null", connectedStatus);
        assertNotNull("LockStatus Switch is null", lockStatus);
        assertNotNull("PopupMenuButton Button is null", popupMenuButton);
    }*/
}
