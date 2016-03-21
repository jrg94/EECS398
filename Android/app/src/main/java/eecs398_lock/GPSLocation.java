package eecs398_lock;

import java.text.DecimalFormat;

/**
 * Created by JRG94 on 2/17/2016.
 * A public class to act as the location of lock
 */
public class GPSLocation {

    // The latitude and longitude values are in degrees
    private double latitude;
    private double longitude;

    public GPSLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String toString() {
        DecimalFormat clean = new DecimalFormat("#.####");
        String lat = clean.format(latitude);
        String lon = clean.format(longitude);
        return String.format("%s, %s", lat, lon);
    }
}
