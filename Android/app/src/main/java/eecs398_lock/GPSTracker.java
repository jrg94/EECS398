package eecs398_lock;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Collection;

/**
 * Created by JRG94 on 4/17/2016.
 *
 * A class which implements the LocationListener interface
 */
public class GPSTracker implements LocationListener {

    /* Brief list of helpful fields */
    private Context mContext;
    private Collection<SmartLock> locks;    // The collection of locks
    private double lastLatitude;
    private double lastLongitude;

    /* Constant for specifying the trigger distance from the lock */
    private static final double DIST_FROM_LOCK_TO_KEY = 50;

    /**
     * The default constructor for the LocationListener
     * @param mContext the app's context
     * @param locks the list of locks
     */
    public GPSTracker(Context mContext, Collection<SmartLock> locks) {
        this.mContext = mContext;
        this.locks = locks;
    }

    /**
     * Retrieves the last recorded latitude
     * @return the lastLatitude field
     */
    public double getLastLatitude() {
        return lastLatitude;
    }

    /**
     * Retrieves the last recorded longitude
     * @return the lastLongitude field
     */
    public double getLastLongitude() {
        return lastLongitude;
    }

    /**
     * Updates fields on location change
     * @param location the object containing data about the current location
     */
    @Override
    public void onLocationChanged(Location location) {

        lastLatitude = location.getLatitude();
        lastLongitude = location.getLongitude();

        String msg = null;

        // Runs through list of locks and calculates their distance from the device
        for (SmartLock sl : locks) {
            double distance = sl.computeDistanceFromKey(new GPSLocation(location.getLatitude(), location.getLongitude()));
            if (distance < DIST_FROM_LOCK_TO_KEY) {
                msg = "You are " + distance + " meters from " + sl.getLabel();
            }
        }

        if (msg != null) {
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Currently not implemented
     * @param provider the gps/network provider
     * @param status the status code
     * @param extras a bundle of extra data
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Prints a message when GPS gets enabled
     * @param provider the network/gps provider
     */
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "GPS has been activated", Toast.LENGTH_SHORT).show();
    }

    /**
     * Asks the user to enable GPS and prints a message to let the user
     * know that GPS has been deactivated
     * @param provider the network/gps provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(intent);
        Toast.makeText(mContext, "GPS has been deactivated", Toast.LENGTH_SHORT).show();
    }
}
