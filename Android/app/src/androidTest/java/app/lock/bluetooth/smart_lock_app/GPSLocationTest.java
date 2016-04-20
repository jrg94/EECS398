package app.lock.bluetooth.smart_lock_app;

import org.junit.Test;

import eecs398_lock.GPSLocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by JRG94 on 4/20/2016.
 */
public class GPSLocationTest {

    private static final double TEST_LATITUDE = 50.134566;
    private static final double TEST_LONGITUDE = -80.674321;
    private static final double EPSILON = .01;

    /**
     * Tests the behavior of the getters for lat and lon
     */
    @Test
    public void testGetLatitudeAndLongitude() {
        GPSLocation test = new GPSLocation(TEST_LATITUDE, TEST_LONGITUDE);

        assertEquals(test.getLatitude(), TEST_LATITUDE, EPSILON);
        assertEquals(test.getLongitude(), TEST_LONGITUDE, EPSILON);
    }

    /**
     * Tests the behavior of toString
     */
    @Test
    public void testToString() {
        GPSLocation test = new GPSLocation(TEST_LATITUDE, TEST_LONGITUDE);

        assertEquals(test.toString(), 50.13457 + ", " + -80.67432);
    }
}
