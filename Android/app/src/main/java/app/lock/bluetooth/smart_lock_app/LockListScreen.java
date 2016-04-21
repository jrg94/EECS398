package app.lock.bluetooth.smart_lock_app;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import eecs398_lock.BluetoothLockService;
import eecs398_lock.GPSTracker;
import eecs398_lock.LocksAdapter;
import eecs398_lock.SmartLock;
import eecs398_lock.SmartLockManager;

/**
 * This is the main Activity that displays the list of locks for a user
 */
public class LockListScreen extends Activity {

    /* Debugging */
    private static final String TAG = "LockListScreen";

    /* Message types sent from the BluetoothLockService Handler */
    public static final int LOCK_STATE_CHANGE = 1;
    public static final int LOCK_READ = 2;
    public static final int LOCK_WRITE = 3;
    public static final int LOCK_DEVICE_ADDRESS = 4;
    public static final int LOCK_TOAST = 5;

    /* Key names received from the BluetoothLockService Handler */
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String TOAST = "toast";

    /* Intent request codes */
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    /* Time and distance constants for location updates */
    private static final int LOC_UPDATE_TIME_MS = 3000;
    private static final int LOC_UPDATE_DIST_MS = 25;

    /* Global fields */
    private BluetoothLockService mLockService = null;       // Member object for the lock services
    private BluetoothAdapter mBluetoothAdapter = null;      // Local Bluetooth adapter
    private SmartLockManager lockManager = null;            // The manager of all the locks
    private LocksAdapter mLockArrayAdapter;                 // The adapter for the grid of locks
    private String currentAddress = "??:??:??:??:??:??";    // An empty address
    private LocationManager locationManager = null;         // Handles location services for the app
    private GPSTracker gpsTracker = null;                   // The GPS listener

    public SmartLockManager getLockManager() {
        return lockManager;
    }

    public LocksAdapter getLocksAdapter() {
        return mLockArrayAdapter;
    }

    /**
     * The onCreate method which can be overridden in all Activity classes
     * In our case, we treat it like a constructor and initialize various fields
     * like the bluetooth adapter and the smart lock manager
     * We also inform the user if bluetooth is unavailable and kill the app if so
     *
     * @param savedInstanceState the saved mapping of String values to parcelable types
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Initialize the saved instance and send that information to the log
        Log.d(TAG, "+++ ON CREATE +++");
        super.onCreate(savedInstanceState);

        // Set up the window layout
        setContentView(R.layout.lock_list_screen);

        /* Initialize some variables */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();                               // Get local Bluetooth adapter
        lockManager = new SmartLockManager();                                                   // Initialize our lock manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);    // Initialize the location manager

        // lockManager.localWipe(this);

        // Load data from file
        lockManager.localLoad(this);

        // lockManager.addLock(mBluetoothAdapter.getRemoteDevice("98:76:B6:00:88:A8"));

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        if (locationManager == null) {
            Toast.makeText(this, "Location services are not available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * The onStart method which can be overridden in all Activity classes
     * In our case, the method handles setting up bluetooth if it is not
     * already
     * Then it initializes the lock screen and bluetooth service
     */
    @Override
    public void onStart() {

        // Call onStart and print this information to the log
        Log.e(TAG, "++ ON START ++");
        super.onStart();

        if (locationManager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gpsTracker = new GPSTracker(this, lockManager.getLocks().values());
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_UPDATE_TIME_MS, 0, gpsTracker);
            }
        }

        // If BT is not on, request that it be enabled.
        // setupLockScreenAndService() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Otherwise, setup the lock session
        else {
            if (mLockService == null) {
                setupLockScreenAndService();
            }
        }
    }

    /**
     * The onResume method which can be overridden in all Activity classes
     * In our case, the method just makes sure that bluetooth is enabled
     * and begins the BluetoothLockService if it hasn't already been enabled
     */
    @Override
    public synchronized void onResume() {

        // Call onResume and print this information to the log
        Log.e(TAG, "+ ON RESUME +");
        super.onResume();


        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mLockService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mLockService.getState() == BluetoothLockService.STATE_NONE) {
                // Start the Bluetooth lock services
                mLockService.start();
            }
        }
    }

    /**
     * Builds the lock screen dynamically using an adapter
     * Initializes bluetooth service
     */
    private void setupLockScreenAndService() {
        Log.d(TAG, "setupLockScreenAndService()");

        // Initialize the array adapter for the lock list
        mLockArrayAdapter = new LocksAdapter(this, lockManager.getLocks());
        GridView mLockView = (GridView) findViewById(R.id.gridView);
        mLockView.setAdapter(mLockArrayAdapter);
        Log.e(TAG, mLockArrayAdapter.getCount() + "");

        // Initialize the BluetoothLockService to perform bluetooth connections
        mLockService = new BluetoothLockService(mHandler);
    }

    /**
     * Creates a popup window
     * @param view
     */
    public void showPopUp(final View view, final SmartLock lock) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Make the window popup
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final PopupWindow popupMenu = new PopupWindow(inflator.inflate(R.layout.popup_menu, null, false), (int)(size.x/1.5), size.y/3, true);
        popupMenu.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Get lock name and set it
        EditText nameText = (EditText)popupMenu.getContentView().findViewById(R.id.popup_lock_name);
        nameText.setText(lock.getLabel());
        nameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // If the action is a key-up event on the return key, send the message
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String message = v.getText().toString();
                    lock.setLabel(message);
                    mLockArrayAdapter.notifyDataSetChanged();
                    lockManager.localSave(getApplicationContext());

                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);

                    return true;
                }

                return false;
            }
        });

        // Get the gps location text box and set it
        TextView locationText = (TextView)popupMenu.getContentView().findViewById(R.id.popup_lock_loc);
        locationText.setText(lock.getLocation().toString());

        // Get the id text and set it
        TextView addressText = (TextView)popupMenu.getContentView().findViewById(R.id.popup_id);
        addressText.setText(lock.getMacAddress().substring(0, 8) + "...");

        // Get the close button from this popup window
        Button close = (Button)popupMenu.getContentView().findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close popup window
                popupMenu.dismiss();
            }
        });

        Button unlink = (Button)popupMenu.getContentView().findViewById(R.id.unlink);
        unlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockManager.localDelete(getApplicationContext(), lock.getMacAddress());
                mLockArrayAdapter.notifyDataSetChanged();
                popupMenu.dismiss();
            }
        });
    }

    /**
     *
     */
    @Override
    public synchronized void onPause() {
        // Call onPause and print this information to the log
        Log.e(TAG, "- ON PAUSE -");
        super.onPause();
    }

    /**
     *
     */
    @Override
    public void onStop() {
        // Call onStop and print this information to the log
        Log.e(TAG, "-- ON STOP --");
        super.onStop();
    }

    /**
     *
     */
    @Override
    public void onDestroy() {

        // Call onDestroy and print this information to the log
        Log.e(TAG, "--- ON DESTROY ---");
        super.onDestroy();

        // Kill connection on locks
        for (SmartLock l : lockManager.getLocks().values()) {
            l.setIsConnected(false);
        }

        // Stop the Bluetooth lock services
        if (mLockService != null) {
            mLockService.stop();
        }

        // Save on close
        lockManager.localSave(this);
    }

    /**
     * Enables discoverability for 300 seconds
     */
    private void ensureDiscoverable() {

        // Print this information to the log
        Log.d(TAG, "ensure discoverable");

        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    public void sendMessage(String message) {

        // Check that we're actually connected before trying anything
        if (mLockService.getState() != BluetoothLockService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {

            // Get the message bytes and tell the BluetoothLockService to write
            byte[] send = message.getBytes();
            mLockService.write(send);
        }
    }

    /**
     * The Handler that gets information back from the BluetoothLockService
     * TODO: Make this its own class
     */
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOCK_STATE_CHANGE:

                    Log.d(TAG, "LOCK_STATE_CHANGE: " + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothLockService.STATE_CONNECTED:
                            break;

                        case BluetoothLockService.STATE_CONNECTING:
                            break;

                        case BluetoothLockService.STATE_LISTEN:
                        case BluetoothLockService.STATE_NONE:
                            break;
                    }
                    break;

                case LOCK_WRITE:

                    Log.d(TAG, "LOCK_WRITE");

                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    handleWrite(writeMessage);

                    break;

                case LOCK_READ:

                    Log.d(TAG, "LOCK_READ");

                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    handleRead(readMessage);

                    break;

                case LOCK_DEVICE_ADDRESS:

                    Log.d(TAG, "LOCK_DEVICE_NAME");

                    // Save the connected device's name and write it to the screen
                    String mConnectedDeviceAddress = msg.getData().getString(DEVICE_ADDRESS);
                    CheckForNewLock(mConnectedDeviceAddress);
                    currentAddress = mConnectedDeviceAddress;
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceAddress, Toast.LENGTH_SHORT).show();

                    break;

                case LOCK_TOAST:

                    Log.d(TAG, "LOCK_TOAST");

                    // Write the message data to the screen
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();

                    // This bundle only tells us if we were unable to connect or if we lost connection
                    for (SmartLock lock : lockManager.getLocks().values()) {
                        lock.setIsConnected(false);
                    }
                    getLocksAdapter().notifyDataSetChanged();;

                    break;
            }
        }
    };

    /**
     * A method for handling all incoming data so that it
     * doesn't always get written to the screen
     * @param msg an input string that needs to be handled
     */
    private void handleRead(String msg) {
        if (msg.contains("SUCCESS") || msg.contains("FAILURE")) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if (msg.contains("SUCCESS: Lock")){
                // Just force all locks to true
                for(SmartLock sl:lockManager.getLocks().values()) {
                    sl.setIsLocked(true);
                }
                mLockArrayAdapter.notifyDataSetChanged();
            }
        }
        else if (msg.contains("REQUEST")) {
            sendMessage(getCurrentAddress());
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Retrieves the current device address
     * @return the currentAddress field
     */
    private String getCurrentAddress() {
        return currentAddress;
    }

    /**
     * A method for handling all outgoing data so that
     * it doesn't always get written to the screen
     * @param msg
     */
    private void handleWrite(String msg) {
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mLockService.connect(device);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a lock service session
                    setupLockScreenAndService();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.scan:
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;

            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    }

    /**
     * Runs through the list of connected devices to see if a new one
     * has been added
     */
    private synchronized void CheckForNewLock(String deviceAddress) {

        Log.d(TAG, "A device has been connected. Checking the device list to see if it is new");

        SmartLock sl;

        // Checks the lock hashmap for the deviceAddress
        if (!lockManager.getLocks().containsKey(deviceAddress)) {
            sl = lockManager.addLock(deviceAddress, gpsTracker);
            lockManager.localSave(this);
        }
        else {
            sl = lockManager.getLocks().get(deviceAddress);
        }

        // No longer have to check if connected because we know that it is connected
        // by the time this function is called
        sl.setIsConnected(true);
        sl.setLockUID(this);
        mLockArrayAdapter.notifyDataSetChanged();
    }
}