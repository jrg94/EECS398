package eecs398_lock;

import java.text.DecimalFormat;

/**
 * Created by JRG94 on 2/17/2016.
 * A public class to act as the location of lock
 */
public class GPSLocation {

    /* The latitude and longitude values are in degrees */
    private double latitude;
    private double longitude;

    /**
     * The default constructor which stores the latitude and longitude
     * @param latitude the first measurement for gps coordinates in degrees
     * @param longitude the second measurement for gps coordinates in degrees
     */
    public GPSLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Retrieves the first part of a GPS coordinate pair
     * @return the latitude field
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Retrieves the second part of a GPS coordinate pair
     * @return the longitude field
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * A handy override for displaying the truncated coordinates
     * @return
     */
    public String toString() {
        DecimalFormat clean = new DecimalFormat("#.#####");
        String lat = clean.format(latitude);
        String lon = clean.format(longitude);
        return String.format("%s, %s", lat, lon);
    }
}
