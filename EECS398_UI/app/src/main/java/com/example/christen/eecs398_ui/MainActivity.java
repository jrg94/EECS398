package com.example.christen.eecs398_ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;


public class MainActivity extends Activity {

    private final static int REQUEST_ENABLE_BT = 1;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the bluetooth object from the intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // TODO: Do something with the list of devices (See Android.Bluetooth documentation)
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBluetooth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Sets up the bluetooth on this device
     *
     * @return true if bluetooth setup was successful
     */
    public boolean setupBluetooth() {
        System.out.println("Setting up Bluetooth");
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println("Acquired the default adapter");

        // Checks to see if bluetooth is supported by this device
        // Emulator cannot test this
        if (adapter == null) {
            // TODO: Throw an exception
            System.out.println("The adapter is null");
            return false;
        }

        // Checks to see if bluetooth is enabled
        if (!adapter.isEnabled()) {
            System.out.println("The adapter is not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Query list of paired devices
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through list of devices
            for (BluetoothDevice device : pairedDevices) {
                // TODO: Do something with the list of devices (See Android.Bluetooth documentation
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            //BluetoothAdapter BT = BluetoothAdapter.getDefaultAdapter();
            //String address = BT.getAddress();
            //String name = BT.getName();
            //String toastText = name + " : " + address;
            //Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
        }
    }
}
