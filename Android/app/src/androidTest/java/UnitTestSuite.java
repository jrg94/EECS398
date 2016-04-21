import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import app.lock.bluetooth.smart_lock_app.LockListScreenTest;
import app.lock.bluetooth.smart_lock_app.SplashScreenTest;
import eecs398_lock.GPSLocationTest;
import eecs398_lock.LocksAdapterTest;
import eecs398_lock.SmartLockTest;

/**
 * Created by JRG94 on 4/20/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        LockListScreenTest.class,
        SplashScreenTest.class,
        GPSLocationTest.class,
        LocksAdapterTest.class,
        SmartLockTest.class})
public class UnitTestSuite {
}
