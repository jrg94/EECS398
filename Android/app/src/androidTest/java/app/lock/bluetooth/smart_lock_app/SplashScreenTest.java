package app.lock.bluetooth.smart_lock_app;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by JRG94 on 4/20/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashScreenTest {

    // TODO: use the damn strings file to make these consistent
    private static final String PASSCODE_TEXT_DEFAULT = "Please Enter Your Passcode";
    private static final String PASSCODE_TEXT_CHANGE1 = "New Password Mode:\nEnter Old Password";
    private static final String PASSCODE_TEXT_CHANGE2 = "New Password Mode:\nNow Enter New Password";
    private static final String PASSCODE_TEXT_CHANGE3 = "Successfully Changed Password";
    private static final String PASSCODE_TEXT_CHANGE4 = "New Password Mode FAILED:\nTry Again";

    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule<>(SplashScreen.class);

    /**
     * Runs a successful login attempt
     */
    @Test
    public void testLoginSuccess() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());

        // Enter correct passcode
        enterPassword(1,2,3,4);

        // Make sure that LockListScreen happens
        intended(hasComponent(LockListScreen.class.getName()));
        Intents.release();
    }

    /**
     * Runs a failing login attempt
     */
    @Test
    public void testLoginFail() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());

        enterPassword(2, 5, 2, 5);

        // Make sure LockListScreen does not happen
        intended(hasComponent(LockListScreen.class.getName()), times(0));
        Intents.release();
    }

    /**
     * Runs a successful password change
     */
    @Test
    public void testChangePasswordSuccess() {
        onView(withId(R.id.imageButtonSettings)).perform(click());

        onView(withId(R.id.passcode_text)).check(matches(withText(PASSCODE_TEXT_CHANGE1)));

        enterPassword(1, 2, 3, 4);

        onView(withId(R.id.passcode_text)).check(matches(withText(PASSCODE_TEXT_CHANGE2)));

        enterPassword(2, 5, 2, 5);

        onView(withId(R.id.passcode_text)).check(matches(withText(PASSCODE_TEXT_CHANGE3)));

        enterPassword(2, 5, 2, 5);
    }

    /**
     * Runs a failing password change
     */
    @Test
    public void testChangePasswordFailure() {

        // Go into password change mode
        onView(withId(R.id.imageButtonSettings)).perform(click());

        // Enter wrong password
        enterPassword(5, 5, 5, 5);

        // Check that app thinks this is incorrect
        onView(withId(R.id.passcode_text)).check(matches(withText(PASSCODE_TEXT_CHANGE4)));
    }

    /**
     * A helper method which enters the correct password
     */
    private void enterPassword(int first, int second, int third, int fourth) {
        onView(withText(String.valueOf(first))).perform(click());
        onView(withText(String.valueOf(second))).perform(click());
        onView(withText(String.valueOf(third))).perform(click());
        onView(withText(String.valueOf(fourth))).perform(click());
    }
}
