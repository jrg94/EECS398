package com.example.christen.eecs398_ui;

/**
 * Created by JRG94 on 2/17/2016.
 * The representation of the behavior of the SmartLock
 * in code
 */
public class SmartLock {

    // FIELDS //

    private int id;
    private String address;
    private GPSLocation location;
    private boolean isLocked;

    // CONSTRUCTORS //

    public SmartLock(int id, GPSLocation location) {
        this.id = id;
        this.location = location;
        // Determine address from GPSLocation
    }

    public SmartLock(int id, double latitude, double longitude) {
        this.id = id;
        this.location = new GPSLocation(latitude, longitude);
        // Determine address from GPSLocation
    }

    // GETTER/SETTERS //

    /**
     * The ID getter method
     * There exists no setter method because we do not
     * want to be able to change the ID for this lock
     * @return the identification number for this lock
     */
    public int getID() {
        return this.id;
    }

    /**
     * The address getter method
     * @return the address of this lock
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * The address setter method
     * @param address a string that marks the general location of the lock
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * The location getter method
     * @return the GPSLocation for the lock
     */
    public GPSLocation getLocation() {
        return this.location;
    }

    /**
     * The location setter method
     * @param location the GPSLocation of the lock
     */
    public void setLocation(GPSLocation location) {
        this.location = location;
    }

    // FUNCTIONALITY //

    /**
     * Toggles the state of the lock
     * @return the state of the lock after the toggle
     */
    public boolean toggleLock() {
        /**
         * TODO: Add functionality to the UI so users can visually see the state of the lock
         * TODO: This code may not get added here, but be aware that it will need to be added
         */
        isLocked = !isLocked;
        return isLocked;
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
}
