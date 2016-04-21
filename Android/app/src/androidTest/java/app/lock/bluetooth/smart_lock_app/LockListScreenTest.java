package app.lock.bluetooth.smart_lock_app;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.EditorAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import eecs398_lock.GPSLocation;
import eecs398_lock.GPSTracker;
import eecs398_lock.LocksAdapter;
import eecs398_lock.SmartLock;
import eecs398_lock.SmartLockManager;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by JRG94 on 4/20/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LockListScreenTest {

    private SmartLockManager lockManager;
    private LocksAdapter locksAdapter;
    private SmartLock testLock;
    private SmartLock adaLock;
    private Context mContext;

    private static final String LABEL = "????";
    private static final String TEST_MAC_ADDRESS = "AA:BB:CC:DD:EE:FF";
    private static final String ADAFRUIT_MAC_ADDRESS = "98:76:B6:00:88:A8";

    @Rule
    public ActivityTestRule<LockListScreen> mActivityRule = new ActivityTestRule<>(LockListScreen.class);

    /**
     * Called before every test
     */
    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        mContext = instrumentation.getTargetContext();
        lockManager = mActivityRule.getActivity().getLockManager();
        locksAdapter = mActivityRule.getActivity().getLocksAdapter();
        testLock = lockManager.addLock(TEST_MAC_ADDRESS, new GPSTracker(mContext, lockManager.getLocks().values()));
        testLock.setLabel(LABEL);
        adaLock = lockManager.getLocks().get(ADAFRUIT_MAC_ADDRESS);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locksAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Tests the open device list button
     */
    @Test
    public void testOpenDeviceList() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());

        // Open options menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        // Click connect a device
        onView(withText("Connect a device")).perform(click());

        // Make sure that DeviceListActivity happens
        intended(hasComponent(DeviceListActivity.class.getName()));
        Intents.release();
    }

    /**
     * Tests the close button on the popup menu
     */
    @Test
    public void testPopupMenuClose() {

        // Finds the popup menu button for our test lock and clicks it
        onData(is(testLock)).onChildView(withId(R.id.popup_lock_menu_button)).perform(click());

        // Checks that the popup is displayed
        onView(withId(R.id.popup_menu)).check(matches(isDisplayed()));

        // Closes the popup window
        onView(withId(R.id.close)).perform(click());

        // Checks for popup existence
        onView(withId(R.id.popup_menu)).check(doesNotExist());
    }

    /**
     * Tests the unlink button on the popup menu
     */
    @Test
    public void testPopupMenuUnlink() {
        // Finds the popup menu button for our test lock and clicks it
        onData(is(testLock)).onChildView(withId(R.id.popup_lock_menu_button)).perform(click());

        // Deletes the lock
        onView(withId(R.id.unlink)).perform(click());

        // Checks for popup existence
        onView(withId(R.id.popup_menu)).check(doesNotExist());

        // Checks for locks existence in hashmap - JUnit style
        assertFalse(lockManager.getLocks().containsValue(testLock));

        // Re-adds lock
        testLock = lockManager.addLock(TEST_MAC_ADDRESS, new GPSTracker(mContext, lockManager.getLocks().values()));

        // Check that it was properly added back into the map
        assertTrue(lockManager.getLocks().containsValue(testLock));
    }

    /**
     * Requires the test device to pass testing
     * Tests the connect feature
     */
    @Test
    public void testConnect() {
        // Open options menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        // Click connect a device
        onView(withText("Connect a device")).perform(click());

        // Attempt to connect to arduino
        onData(hasToString(startsWith("Adafruit"))).inAdapterView(withId(R.id.paired_devices)).perform(click());

        // Wait because connecting takes time
        SystemClock.sleep(4000);

        // Check that we're connected
        onData(is(adaLock)).onChildView(withId(R.id.connectedStatus)).check(matches(withText("connected")));
    }

    /**
     * Tests that label gets saved on change
     */
    @Test
    public void testChangeLabel() {
        // Finds the popup menu button for our test lock and clicks it
        onData(is(testLock)).onChildView(withId(R.id.popup_lock_menu_button)).perform(click());

        // Change label by clearing text, typing text, then pressing done
        onView(withId(R.id.popup_lock_name)).perform(clearText(), typeText("Garage Door"), pressImeActionButton());

        // Closes the popup window
        onView(withId(R.id.close)).perform(click());

        // Tests that label has changed
        onData(is(testLock)).onChildView(withId(R.id.lockLabel)).check(matches(withText("Garage Door")));
    }

    /**
     * Called after every test
     */
    @After
    public void tearDown() {
        lockManager.localDelete(mContext, testLock.getMacAddress());
    }
}
