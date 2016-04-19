package eecs398_lock;

import java.util.UUID;

import app.lock.bluetooth.smart_lock_app.LockListScreen;

/**
 * Created by JRG94 on 2/17/2016.
 * The representation of the SmartLock in code
 * All data for each lock will be held in this class
 * Functionality to interface between the physical lock
 * and the app will also be held in this class
 */
public class SmartLock {

    /* Smart Lock Criteria */
    private UUID id;
    private String label;
    private GPSLocation location;
    private String macAddress;
    private boolean isLocked;
    private boolean isConnected;

    /* Command Code Constants */
    private static final int SET_CODE = 0xDEAD;
    private static final int LOCK_CODE = 0xEF93;
    private static final int UNLOCK_CODE = 0x081D;

    /* String Constants */
    private static final String EMPTY_LOCK_NAME = "Insert Label Here";
    private static final String CMD_CHAR = "*";
    private static final String CMD_FORMAT = "%s%d:%d:%s";

    /* Hardcoded data for GPS proof-of-concept */
    private static final double SUITE_314C_LAT = 41.513417;
    private static final double SUITE_314C_LON = -81.60438909999999;

    /**
     * The standard constructor which takes in the Bluetooth
     * Device and stores its macAddress for later use
     * @param device the bluetooth device that this code represents
     */
    public SmartLock(String macAddress) {
        this.macAddress = macAddress;
        this.id = UUID.randomUUID();
        this.label = EMPTY_LOCK_NAME;
        this.location = new GPSLocation(SUITE_314C_LAT, SUITE_314C_LON);
        this.isLocked = true;
        this.isConnected = false;
    }

    /**
     * A more advanced constructor which is used to
     * store the location of the physical lock
     * @param device the bluetooth device that this code represents
     * @param latitude the latitude of the physical device
     * @param longitude the longitude of the physical device
     */
    public SmartLock(String macAddress, double latitude, double longitude) {
        this.macAddress = macAddress;
        this.id = UUID.randomUUID();
        this.label = EMPTY_LOCK_NAME;
        this.location = new GPSLocation(latitude, longitude);
        this.isLocked = true;
        this.isConnected = false;
    }

    /**
     * Retrieves the UUID that is generated at lock creation
     * @return the UUID for this lock
     */
    public UUID getID() {
        return this.id;
    }

    /**
     * Retrieves the name of this lock
     * @return the label field for the lock
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the name of this lock
     * @param label the name to give to this lock
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Retrieves the gps location of this lock
     * @return the location field for this lock
     */
    public GPSLocation getLocation() {
        return this.location;
    }

    /**
     * Retrieves the state of the lock (locked or unlocked)
     * @return the isLocked field for this lock
     */
    public boolean getIsLocked() {
        return isLocked;
    }

    /**
     * Sets the state of the lock (locked or unlocked)
     * @param isLocked the state of the lock as a boolean
     */
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * Retrieves the state of the bluetooth connection between the
     * physical lock and the app
     * @return the isConnected field for this lock
     */
    public boolean getIsConnected() {
        return isConnected;
    }

    /**
     * Sets the state of connection between the physical lock
     * and the app
     * @param isConnected ths state of the bluetooth connection as a boolean
     */
    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    /**
     * The MAC Address for the bluetooth device associated
     * with this lock
     * @return the macAddress field
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Overrides the equals method to test if two locks are the same
     * TODO: Make this better (id is unreliable and not used)
     * @param o the object of comparison
     * @return true if the two locks are equivalent
     */
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

    /**
     * Returns the lock as a string
     * @return the lock as a string
     */
    @Override
    public String toString() {
        return String.format("%s: %s", this.id.toString(), getLocation().toString());
    }

    /**
     * Toggles the state of the lock
     * @return the state of the lock after the toggle
     */
    public boolean unlock(LockListScreen lls) {

        // Sends the unlock message to the arduino
        lls.sendMessage(String.format(CMD_FORMAT, CMD_CHAR, UNLOCK_CODE, macAddress.length(), macAddress));

        // Set isLocked to false
        isLocked = false;

        return isLocked;
    }

    /**
     * The setup function used during lock setup
     * This command is passed to the device to let it know
     * that you want to establish a permanent connection with
     * a password (the password is the MAC address)
     * @param lls the lock list activity
     */
    public void setLockUID(LockListScreen lls) {
        lls.sendMessage(String.format(CMD_FORMAT, CMD_CHAR, SET_CODE, macAddress.length(), macAddress));
    }

    /**
     * Computes the haversine distance between the lock
     * and the key (device holding the key)
     * @param keyLoc the location of the key
     * @return the distance between the lock and the key
     */
    public double computeDistanceFromKey(GPSLocation keyLoc) {
        // The radius of the earth in meters
        double radius = 6_371_000;

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
}
