package app.lock.bluetooth.smart_lock_app;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
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

    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule<>(SplashScreen.class);

    /**
     * Runs a login attempt
     */
    @Test
    public void testLogin() {
        // Enter passcode
        enterPassword(1,2,3,4);
    }

    @Test
    public void testChangePassword() {
        onView(withId(R.id.imageButtonSettings)).perform(click());

        onView(withId(R.id.passcode_text)).check(matches(withText(PASSCODE_TEXT_CHANGE1)));

        enterPassword(1,2,3,4);

        onView(withId(R.id.passcode_text)).check(matches(withText(PASSCODE_TEXT_CHANGE2)));
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
