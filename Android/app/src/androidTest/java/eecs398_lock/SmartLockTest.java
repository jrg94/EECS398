package eecs398_lock;

import org.junit.Test;

import eecs398_lock.SmartLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by JRG94 on 4/18/2016.
 */
public class SmartLockTest {

    private static final String TEST_MAC_ADDRESS = "AA:BB:CC:DD:EE:FF";

    /**
     * Runs a test on the getters for latitude and longitude
     */
    @Test
    public void testGetLatitudeAndLongitude() {
        double latitude = 40.0;
        double longitude = 50.0;

        SmartLock test = new SmartLock(TEST_MAC_ADDRESS, latitude, longitude);
        assertEquals(test.getLocation().getLatitude(), latitude, .01);
        assertEquals(test.getLocation().getLongitude(), longitude, .01);
    }

    /**
     * Runs a test on the getter and setter for label
     */
    @Test
    public void testGetAndSetLabel() {
        SmartLock test = new SmartLock(TEST_MAC_ADDRESS);

        assertEquals(test.getLabel(), "Insert Label Here");

        test.setLabel("Front Door");
        assertEquals(test.getLabel(), "Front Door");
    }

    /**
     * Runs a test on the getter and setter for isLocked
     */
    @Test
    public void testGetAndSetIsLocked() {
        SmartLock test = new SmartLock(TEST_MAC_ADDRESS);

        // Initial state of the lock should be true
        assertTrue(test.getIsLocked());

        test.setIsLocked(false);
        assertFalse(test.getIsLocked());
    }

    /**
     * Runs a test on the getter and setter for isConnected
     */
    @Test
    public void testGetAndSetIsConnected() {
        SmartLock test = new SmartLock(TEST_MAC_ADDRESS);

        // Initial state of the lock should be disconnected
        assertFalse(test.getIsConnected());

        test.setIsConnected(true);
        assertTrue(test.getIsConnected());
    }

    /**
     * Runs a test on the getter for macAddress
     */
    @Test
    public void testGetMacAddress() {
        SmartLock test = new SmartLock(TEST_MAC_ADDRESS);

        assertEquals(test.getMacAddress(), TEST_MAC_ADDRESS);
    }

    /**
     * IMPORTANT TEST
     * This test makes sure that all of the other tests
     * actually report correct results
     */
    @Test
    public void testEquals() {
        SmartLock test1 = new SmartLock(TEST_MAC_ADDRESS);
        SmartLock test2 = new SmartLock(TEST_MAC_ADDRESS);

        assertTrue(test1.equals(test2));
    }

    /**
     * Tests the expected behavior of toString()
     */
    @Test
    public void testToString() {
        SmartLock test = new SmartLock(TEST_MAC_ADDRESS);

        assertEquals(test.toString(), TEST_MAC_ADDRESS + ": " + test.getLocation().toString());
    }
}
