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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eecs398_lock.BluetoothLockService;
import eecs398_lock.GPSLocation;
import eecs398_lock.LocksAdapter;
import eecs398_lock.SmartLock;
import eecs398_lock.SmartLockManager;

/**
 * This is the main Activity that displays the current chat session.
 */
public class LockListScreen extends Activity {

    // Debugging
    private static final String TAG = "LockListScreen";

    // Message types sent from the BluetoothLockService Handler
    public static final int LOCK_STATE_CHANGE = 1;
    public static final int LOCK_READ = 2;
    public static final int LOCK_WRITE = 3;
    public static final int LOCK_DEVICE_NAME = 4;
    public static final int LOCK_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Member object for the chat services
    private BluetoothLockService mLockService = null;

    // The manager of all the locks
    private SmartLockManager lockManager = null;

    private LockListScreen mLLS = this;

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

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize our lock manager
        lockManager = new SmartLockManager();

        // Load data from file
        lockManager.localLoad(this);

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
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

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Otherwise, setup the chat session
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
                // Start the Bluetooth chat services
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
        LocksAdapter mLockArrayAdapter = new LocksAdapter(this, new ArrayList<SmartLock>(lockManager.getLocks().values()));
        GridView mLockView = (GridView) findViewById(R.id.gridView);
        mLockView.setAdapter(mLockArrayAdapter);
        Log.e(TAG, mLockArrayAdapter.getCount() + "");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mLockService = new BluetoothLockService(this, mHandler);
    }

    /**
     * Creates a popup window
     * @param view
     */
    public void showPopUp(View view, SmartLock lock) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Make the window popup
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final PopupWindow popupMenu = new PopupWindow(inflator.inflate(R.layout.lock_menu, null, false), (int)(size.x/1.5), size.y/2, true);
        popupMenu.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Get lock name and set it
        EditText nameText = (EditText)popupMenu.getContentView().findViewById(R.id.popup_lock_name);
        nameText.setText(lock.getLabel());

        // Get the gps location text box and set it
        TextView locationText = (TextView)popupMenu.getContentView().findViewById(R.id.popup_lock_loc);
        locationText.setText(lock.getLocation().toString());

        // Get the id text and set it
        TextView idText = (TextView)popupMenu.getContentView().findViewById(R.id.popup_id);
        idText.setText(lock.getID().toString());

        // Get the close button from this popup window
        Button close = (Button)popupMenu.getContentView().findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close popup window
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

        // TODO: Should we save?

        // Call onDestroy and print this information to the log
        Log.e(TAG, "--- ON DESTROY ---");
        super.onDestroy();

        // Stop the Bluetooth chat services
        if (mLockService != null) {
            mLockService.stop();
        }
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

            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mLockService.write(send);
        }
    }

    // TODO: KEEP - use for saving edit text stuff
    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }

                    return true;
                }
            };

    /**
     * The Handler that gets information back from the BluetoothLockService
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

                    // TODO: Do something with writeMessage
                    // BluetoothChat was using it to write to your screen

                    break;

                case LOCK_READ:

                    Log.d(TAG, "LOCK_READ");

                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    // TODO: Do something with readMessage
                    // BluetoothChat was using it to write to your screen
                    Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_SHORT).show();

                    break;

                case LOCK_DEVICE_NAME:

                    Log.d(TAG, "LOCK_DEVICE_NAME");

                    // Save the connected device's name and write it to the screen
                    String mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    CheckForNewLock();

                    break;

                case LOCK_TOAST:

                    Log.d(TAG, "LOCK_TOAST");

                    // Write the message data to the screen
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

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
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mLockService.connect(device);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
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
    private void CheckForNewLock() {

        Log.d(TAG, "A device has been connected. Checking the device list to see if it is new");

        List<BluetoothDevice> devices = mLockService.getDevices();
        for (BluetoothDevice bd : devices) {
            if (!lockManager.getLocks().containsKey(bd.getAddress())) {
                lockManager.addLock(bd.getAddress());
                lockManager.localSave(this);
            }
        }
    }
}