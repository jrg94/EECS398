package app.lock.bluetooth.smart_lock_app;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by JRG94 on 4/20/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LockListScreenTest {

    @Rule
    public ActivityTestRule<LockListScreen> mActivityRule = new ActivityTestRule<>(LockListScreen.class);

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
}
