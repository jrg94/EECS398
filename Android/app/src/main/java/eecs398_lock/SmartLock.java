package eecs398_lock;

import android.bluetooth.BluetoothDevice;
import android.widget.Switch;

import java.util.UUID;

import app.lock.bluetooth.smart_lock_app.LockListScreen;
import app.lock.bluetooth.smart_lock_app.R;

/**
 * Created by JRG94 on 2/17/2016.
 * The representation of the SmartLock in code
 * All data for each lock will be held in this class
 * Functionality to interface between the physical lock
 * and the app will also be held in this class
 */
public class SmartLock {

    // FIELDS //

    private UUID id;
    private String address;
    private String label;
    private GPSLocation location;
    private BluetoothDevice device;
    private boolean isLocked;
    private boolean isInLowPowerMode;
    private boolean isConnected;

    private static final int LOCK_CODE = 0xEF93;
    private static final int UNLOCK_CODE = 0x081D;

    // CONSTRUCTORS //

    public SmartLock(BluetoothDevice device) {
        this.device = device;
        this.id = UUID.randomUUID();
        this.address = "At what address is this lock?";
        this.label = "What would you like to name this lock?";
        this.location = new GPSLocation(Math.random()*180, Math.random()*180);
        this.isLocked = false;
        this.isInLowPowerMode = false;
        this.isConnected = false;
    }

    public SmartLock(BluetoothDevice device, GPSLocation location) {
        this.device = device;
        this.id = UUID.randomUUID();
        this.address = "At what address is this lock?";
        this.label = "What would you like to name this lock?";
        this.location = location;
        this.isLocked = false;
        this.isInLowPowerMode = false;
        this.isConnected = false;
    }

    public SmartLock(BluetoothDevice device, double latitude, double longitude) {
        this.device = device;
        this.id = UUID.randomUUID();
        this.address = "At what address is this lock?";
        this.label = "What would you like to name this lock?";
        this.location = new GPSLocation(latitude, longitude);
        this.isLocked = false;
        this.isInLowPowerMode = false;
        this.isConnected = false;
    }

    // GETTER/SETTERS //

    public UUID getID() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public GPSLocation getLocation() {
        return this.location;
    }

    public void setLocation(GPSLocation location) {
        this.location = location;
    }

    public boolean getIsLocked() { return isLocked; }

    public boolean getIsConnected() { return isConnected; }

    public void setIsConnected(boolean isConnected) { this.isConnected = isConnected; }

    public BluetoothDevice getDevice() { return device; }

    // FUNCTIONALITY //

    @Override
    public boolean equals(Object o) {
        if (o instanceof SmartLock) {
            SmartLock tempLock = (SmartLock)o;

            boolean testID = tempLock.id.equals(this.id);

            if (testID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.id.toString(), getLocation().toString());
    }

    /**
     * Toggles the state of the lock
     * @return the state of the lock after the toggle
     */
    public boolean toggleLock(LockListScreen lls) {
        Switch lockState = (Switch) lls.findViewById(R.id.lockState);

        if (isLocked) {
            lls.sendMessage(String.format("%s%d", "*", UNLOCK_CODE));
        }
        else {
            lls.sendMessage(String.format("%s%d", "*", LOCK_CODE));
        }

        isLocked = !isLocked;

        // TODO: Test to see if the lock has changed state - Report an error if not (exception?)
        return isLocked;
    }

    /**
     * Toggles the low power mode for the BlueTooth Shield
     * @return the state of the low power mode after the toggle
     */
    public boolean toggleLowPowerMode() {
        // TODO: Signal the circuit to flip the state of its low power mode
        // TODO: Set the state of isInLowPowerMode after the signal
        // TODO: Test to see if the lock has changed state - Report an error if not (exception?)
        return isInLowPowerMode;
    }

    /**
     * Computes the haversine distance between the lock
     * and the key (device holding the key)
     * @param keyLoc the location of the key
     * @return the distance between the lock and the key
     */
    public double computeDistanceFromKey(GPSLocation keyLoc) {
        // The radius of the earth in meters
        double radius = 6371000;

        // Converts the coordinates in degrees to radians
        double phi1 = Math.toRadians(this.location.getLatitude());
        double phi2 = Math.toRadians(keyLoc.getLatitude());

        double deltaPhi = Math.toRadians(keyLoc.getLatitude() - this.location.getLatitude());
        double deltaLambda = Math.toRadians(keyLoc.getLongitude() - this.location.getLongitude());

        double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
                   Math.cos(phi1) * Math.cos(phi2) *
                   Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = radius * c;

        return d;
    }

    /**
     * This method should interface directly with the physical lock circuit through BlueTooth
     * @return true if the update was successful
     */
    public boolean updateLockData() {
        // TODO: Establish connection with lock circuit
        // TODO: Update GPSLocation field to ensure the lock hasn't moved
        // TODO: Update isLocked field to ensure lock is in the expected state
        // TODO: Update isInLowPowerMode field to ensure lock is in expected state
        return true;
    }
}
