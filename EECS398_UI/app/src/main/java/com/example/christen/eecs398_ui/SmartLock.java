package com.example.christen.eecs398_ui;

/**
 * Created by JRG94 on 2/17/2016.
 * The representation of the behavior of the SmartLock
 * in code
 */
public class SmartLock {

    private int id;
    private String address;
    private GPSLocation location;

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
}
