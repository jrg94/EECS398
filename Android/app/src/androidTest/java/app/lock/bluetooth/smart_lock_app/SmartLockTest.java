package app.lock.bluetooth.smart_lock_app;

import org.junit.Test;

import eecs398_lock.SmartLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by JRG94 on 4/18/2016.
 */
public class SmartLockTest {

    /**
     * Runs a test on the getters for latitude and longitude
     */
    @Test
    public void testGetLatitudeAndLongitude() {
        double latitude = 40.0;
        double longitude = 50.0;

        SmartLock test = new SmartLock("AA:BB:CC:DD:EE:FF", latitude, longitude);
        assertEquals(test.getLocation().getLatitude(), latitude, .01);
        assertEquals(test.getLocation().getLongitude(), longitude, .01);
    }

}
