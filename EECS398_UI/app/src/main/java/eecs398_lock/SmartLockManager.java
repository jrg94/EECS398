package eecs398_lock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.christen.eecs398_ui.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by JRG94 on 2/17/2016.
 * A class for handling a set of locks
 */
public class SmartLockManager {

    // FIELDS //

    private static final String TAG = "SmartLockManager";
    private static final boolean D = true;

    private ArrayList<SmartLock> locks;
    private static final String PREFS_NAME = "com.example.christen.eecs398_ui";

    // CONSTRUCTORS //

    // An empty constructor for initializes the list of locks
    public SmartLockManager() {
        locks = new ArrayList<SmartLock>();
    }

    // GETTER/SETTERS //

    public ArrayList<SmartLock> getLocks() {
        return locks;
    }

    // METHODS //

    /**
     * The local save method for the set of locks
     * TODO: Create a database version of this that would push data to a server
     * @param context
     */
    public void localSave(Context context) {

        // Create a google json object
        Gson gson = new Gson();

        // Initialize user preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize editor
        SharedPreferences.Editor editor = prefs.edit();

        // Store the number of locks
        editor.putInt(context.getResources().getString(R.string.number_of_locks), locks.size());

        // Run through list of locks
        for (SmartLock lock : locks) {
            // Convert each lock to json
            String lock_json = gson.toJson(lock);

            // Save the new json by an id
            editor.putString(lock.getID() + "", lock_json);
        }

        // Save
        editor.apply();
    }

    /**
     * Loads all of the lock data
     * @param context
     */
    public void localLoad(Context context) {

        // Create a google json object
        Gson gson = new Gson();

        // Initialize user preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        int numLocks = prefs.getInt(context.getResources().getString(R.string.number_of_locks), 0);

        for (int i = 0; i < numLocks; i++) {

            Log.e(TAG, "Reading lock");

            // Generate the lock based on the key
            SmartLock tempLock = gson.fromJson(prefs.getString(i + "", ""), SmartLock.class);

            // If the list does not contain this new lock, add it
            if (!locks.contains(tempLock)) {
                Log.e(TAG, "Adding lock");
                locks.add(tempLock);
            }
        }
    }

    /**
     * Deletes the item based on the key
     * @param context
     * @param key
     */
    public void localDelete(Context context, String key) {

        // Initialize user preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize editor
        SharedPreferences.Editor editor = prefs.edit();

        // Removes a piece of data by the key
        editor.remove(key);

        // Applies the removal
        editor.apply();
    }
}
