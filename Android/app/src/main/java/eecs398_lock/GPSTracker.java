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
 */
public class GPSTracker implements LocationListener {

    private Context mContext;
    private Collection<SmartLock> locks;
    private double lastLatitude;
    private double lastLongitude;

    private static final double DIST_FROM_LOCK_TO_KEY = 50;

    public GPSTracker(Context mContext, Collection<SmartLock> locks) {
        this.mContext = mContext;
        this.locks = locks;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(intent);
        Toast.makeText(mContext, "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }
}
