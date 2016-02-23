package com.example.christen.eecs398_ui;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import eecs398_lock.BluetoothLockService;

/**
 * This activity will display the locks available to the user
 *
 * The apache license is attached above because a majority of this code was
 * borrowed from the BluetoothChat class in the sample provided through Android.
 */
public class LockListScreen extends Activity {

    // FIELDS //

    // Debug tools
    private static final boolean DEBUG = true;
    private static final String TAG = "LockListScreen";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Member fields
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothLockService mLockService = null;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;


    // The Handler that gets information back from the BluetoothLockService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // TODO: Add stuff
            }
        }
    };

    // OVERRIDDEN METHODS //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEBUG) {
            Log.e(TAG, ">>> ON CREATE <<<");
        }

        // Sets up the window layout
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.splash_screen);

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Retrieves the local bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available\nApp is now closing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (DEBUG) {
            Log.e(TAG, ">> ON START <<");
        }

        // If Bluetooth is not enabled, request that it be enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else {
            if (mLockService == null) {
                setupLockService();
            }
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (DEBUG) {
            Log.e(TAG, "> ON RESUME <");
        }

        // The serves to handle the case where onStart has to start bluetooth
        if (mLockService != null) {
            // Service hasn't begun yet
            if (mLockService.getState() == BluetoothLockService.STATE_NONE) {
                mLockService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(DEBUG) {
            Log.e(TAG, "- ON PAUSE -");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(DEBUG) {
            Log.e(TAG, "-- ON STOP --");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth lock services
        if (mLockService != null) {
            mLockService.stop();
        }
        if(DEBUG) {
            Log.e(TAG, "--- ON DESTROY ---");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(DEBUG) {
            Log.d(TAG, "onActivityResult " + resultCode);
        }
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupLockService();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // FUNCTIONALITY //

    private void setupLockService() {
        Log.e(TAG, "setupLockService");

        // TODO: Do things

        // Initialize BluetoothLockService to handle bluetooth connections
        mLockService = new BluetoothLockService(this, mHandler);
    }

    private void ensureDiscoverable() {
        if(DEBUG) {
            Log.d(TAG, "ensure discoverable");
        }
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        // String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        // BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        // mLockService.connect(device, secure);
    }
}
