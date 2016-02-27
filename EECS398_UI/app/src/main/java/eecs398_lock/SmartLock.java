package eecs398_lock;

/**
 * Created by JRG94 on 2/17/2016.
 * The representation of the SmartLock in code
 * All data for each lock will be held in this class
 * Functionality to interface between the physical lock
 * and the app will also be held in this class
 */
public class SmartLock {

    // FIELDS //

    private int id;
    private String address;
    private String label;
    private GPSLocation location;
    private boolean isLocked;
    private boolean isInLowPowerMode;
    // Possible list field for owners

    // CONSTRUCTORS //

    public SmartLock(int id, GPSLocation location) {
        this.id = id;
        this.address = "At what address is this lock?";
        this.label = "What would you like to name this lock?";
        this.location = location;
        this.isLocked = false;
        this.isInLowPowerMode = false;
    }

    public SmartLock(int id, double latitude, double longitude) {
        this.id = id;
        this.address = "At what address is this lock?";
        this.label = "What would you like to name this lock?";
        this.location = new GPSLocation(latitude, longitude);
        this.isLocked = false;
        this.isInLowPowerMode = false;
    }

    // GETTER/SETTERS //

    public int getID() {
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

    // FUNCTIONALITY //

    @Override
    public boolean equals(Object o) {
        if (o instanceof SmartLock) {
            SmartLock tempLock = (SmartLock)o;

            boolean testID = tempLock.id == this.id;

            if (testID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", this.id, getLocation().toString());
    }

    /**
     * Toggles the state of the lock
     * @return the state of the lock after the toggle
     */
    public boolean toggleLock() {
        // TODO: Signal the circuit to flip the state of the lock
        // TODO: Set the state of isLocked after the signal
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
