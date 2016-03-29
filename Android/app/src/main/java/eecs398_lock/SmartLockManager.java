package eecs398_lock;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import app.lock.bluetooth.smart_lock_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JRG94 on 2/17/2016.
 * A class for handling a set of locks
 */
public class SmartLockManager {

    // FIELDS //

    private static final String TAG = "SmartLockManager";
    private static final boolean D = true;

    private HashMap<String, SmartLock> locks;
    private static final String PREFS_NAME = "app.lock.bluetooth.smart_lock";

    // CONSTRUCTORS //

    // An empty constructor for initializes the list of locks
    public SmartLockManager() {
        locks = new HashMap<String, SmartLock>();
    }

    // GETTER/SETTERS //

    public HashMap<String, SmartLock> getLocks() {
        return locks;
    }

    // METHODS //

    public SmartLock addLock(BluetoothDevice device) {
        SmartLock tempLock = new SmartLock(device);
        locks.put(device.getAddress(), tempLock);
        return tempLock;
    }

    /**
     * The local save method for the set of locks
     * TODO: Create a database version of this that would push data to a server
     * @param context
     */
    public void localSave(Context context) {

        Log.e(TAG, "Saving lock list");

        // Create a google json object
        Gson gson = new Gson();

        // Initialize user preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize editor
        SharedPreferences.Editor editor = prefs.edit();

        // Store the number of locks
        editor.putInt(context.getResources().getString(R.string.number_of_locks), locks.size());

        // Store the array of keys
        editor.putString(context.getResources().getString(R.string.list_of_keys), gson.toJson(locks.keySet()));

        Iterator it = locks.entrySet().iterator();

        // Run through list of locks
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            // Convert each lock to json
            String lock_json = gson.toJson((SmartLock)pair.getValue());

            // Save the new json by an id
            editor.putString((String)pair.getKey(), lock_json);
        }

        // Save
        editor.apply();
    }

    /**
     * Loads all of the lock data
     * @param context
     */
    public void localLoad(Context context) {

        Log.e(TAG, "Loading lock list");

        // Create a google json object
        Gson gson = new Gson();

        // Initialize user preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Test if the file is corrupt
        if (isCorrupt(gson, prefs, context)) {
            return;
        }

        // Otherwise, load the file
        int numLocks = prefs.getInt(context.getResources().getString(R.string.number_of_locks), -1);

        String list_of_keys = prefs.getString(context.getResources().getString(R.string.list_of_keys), "");

        Set<String> keySet = gson.fromJson(list_of_keys, new TypeToken<Set<String>>() {}.getType());
        String[] keys = keySet.toArray(new String[keySet.size()]);

        for (int i = 0; i < numLocks; i++) {

            Log.e(TAG, "Reading lock");

            // Generate the lock based on the key
            SmartLock tempLock = gson.fromJson(prefs.getString(keys[i], ""), SmartLock.class);

            // If the list does not contain this new lock, add it
            if (!locks.containsValue(tempLock)) {
                Log.e(TAG, "Adding lock");
                locks.put(keys[i], tempLock);
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

    /**
     *
     * @return true if the save file is corrupt
     */
    public boolean isCorrupt(Gson gson, SharedPreferences prefs, Context context) {

        // The number of locks in the file
        int numLocks = prefs.getInt(context.getResources().getString(R.string.number_of_locks), -1);

        // Retrieve the json of the list of keys from the
        String list_of_keys = prefs.getString(context.getResources().getString(R.string.list_of_keys), "");

        if (numLocks < 0 || list_of_keys.length() == 0) {
            Log.e(TAG, "NumLocksKeyListMismatch: Save file is corrupt");
            return true;
        }

        return false;
    }
}
