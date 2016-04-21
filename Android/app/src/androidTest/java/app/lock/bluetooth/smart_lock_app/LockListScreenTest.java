package app.lock.bluetooth.smart_lock_app;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by JRG94 on 4/20/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LockListScreenTest {

    private SmartLockManager lockManager;
    private LocksAdapter locksAdapter;
    private SmartLock testLock;
    private Context mContext;

    private static final String LABEL = "????";
    private static final String MAC_ADDRESS = "AA:BB:CC:DD:EE:FF";

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
        testLock = lockManager.addLock(MAC_ADDRESS, new GPSTracker(mContext, lockManager.getLocks().values()));
        testLock.setLabel(LABEL);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locksAdapter.notifyDataSetChanged();
            }
        });
    }

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

    @Test
    public void testPopupMenu() {

        // Finds the popup menu button for our test lock and clicks it
        onData(is(testLock)).onChildView(withId(R.id.popup_lock_menu_button)).perform(click());

        // Checks that the popup is displayed
        onView(withId(R.id.popup_menu)).check(matches(isDisplayed()));

    }

    /**
     * Called after every test
     */
    @After
    public void tearDown() {
        lockManager.localDelete(mContext, testLock.getMacAddress());
    }
}
