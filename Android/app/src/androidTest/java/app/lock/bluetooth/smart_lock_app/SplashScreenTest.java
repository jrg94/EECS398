package app.lock.bluetooth.smart_lock_app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by JRG94 on 4/20/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashScreenTest {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule<>(SplashScreen.class);

    /**
     * Runs a login attempt
     */
    @Test
    public void testLogin() {
        // Enter passcode
        onView(withId(R.id.buttonOne)).perform(click());
        onView(withId(R.id.buttonTwo)).perform(click());
        onView(withId(R.id.buttonThree)).perform(click());
        onView(withId(R.id.buttonFour)).perform(click());
    }
}
