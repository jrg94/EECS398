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
}
