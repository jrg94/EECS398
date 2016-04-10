package app.lock.bluetooth.smart_lock_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The main activity for the application
 * This activity will display the logo and the pin pad
 * Login is achieved through this screen
 */
public class SplashScreen extends Activity {

    // FIELDS //

    // Variables
    private List<Button> keypad;
    private int pressCount = 0;
    private int[] attemptedLogin = new int[4];

    // Constants
    private static final int[] passcode = {1, 2, 3, 4};

    private static final int[] BUTTON_IDS = {
            R.id.buttonOne,
            R.id.buttonTwo,
            R.id.buttonThree,
            R.id.buttonFour,
            R.id.buttonFive,
            R.id.buttonSix,
            R.id.buttonSeven,
            R.id.buttonEight,
            R.id.buttonNine
    };

    // OVERRIDDEN METHODS //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets up the window layout
        setContentView(R.layout.splash_screen);

        keypad = new ArrayList<Button>(BUTTON_IDS.length);

        for (int id : BUTTON_IDS) {
            Button b = (Button)findViewById(id);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptedLogin[pressCount] = Integer.parseInt(((Button)v).getText().toString());
                    pressCount++;

                    // We have reached the required number of digits
                    if (pressCount == passcode.length) {

                        // Reset pressCount
                        pressCount = 0;

                        // Test that the two passcodes match
                        for (int i = 0; i < passcode.length; i++) {

                            // If at any point they don't match, return
                            if (passcode[i] != attemptedLogin[i]) {
                                Toast.makeText(SplashScreen.this, "Failed to enter the correct passcode", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        // If so, change to the next activity (lock list screen)
                        startActivity(new Intent(SplashScreen.this, LockListScreen.class));
                    }
                }
            });
            keypad.add(b);
        }

        RatingBar buttonsClicked = (RatingBar)findViewById(R.id.buttons_clicked);
        buttonsClicked
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
