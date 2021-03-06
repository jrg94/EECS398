package eecs398_lock;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import app.lock.bluetooth.smart_lock_app.LockListScreen;
import app.lock.bluetooth.smart_lock_app.R;

import java.util.HashMap;

/**
 * Created by JRG94 on 2/25/2016.
 * Overrides the functionality of ArrayAdapter to provide
 * custom functionality for the way locks are displayed
 */
public class LocksAdapter extends BaseAdapter {

    /* Adapter Fields */
    private Context mContext;                       // Holds the app context
    private long mLastClickTime;                    // Variable to track event time
    private HashMap<String, SmartLock> locks;       // Holds the hashmap of locks
    private String[] keys;                          // Holds a list of keys

    /* Constants */
    private static final String CONNECTED_COLOR = "#029E02";
    private static final String DISCONNECTED_COLOR = "#E53715";

    /**
     * Initializes the variables for this adapter
     * @param context the app context
     * @param locks the map of locks
     */
    public LocksAdapter(Context context, HashMap<String, SmartLock> locks) {
        mLastClickTime = 0;
        this.locks = locks;
        keys = this.locks.keySet().toArray(new String[locks.size()]);
        this.mContext = context;
    }

    /**
     * Retrieves the number of elements in the HashMap
     * @return an integer that represents the number of elements in the hashmap
     */
    @Override
    public int getCount() {
        return locks.size();
    }

    /**
     * Retrieves the item at the position
     * @param position an integer that serves as an index into the HashMap
     * @return the object at the position
     */
    @Override
    public Object getItem(int position) {
        return locks.get(keys[position]);
    }

    /**
     * Retrieves the id of the item at a position
     * @param position the index of the item
     * @return the id of the item which happens to just be the position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * The update function which establishes each view
     * @param position the index of the view
     * @param convertView the actual view that is being updated
     * @param parent the parent of the view
     * @return the view that was updated
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // CRUCIAL
        keys = this.locks.keySet().toArray(new String[locks.size()]);

        // Retrieve lock from position
        final SmartLock lock = (SmartLock) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lock_ui, parent, false);
        }

        // Initialize UI elements
        TextView lockLabel = (TextView) convertView.findViewById(R.id.lockLabel);
        TextView connectedStatus = (TextView) convertView.findViewById(R.id.connectedStatus);
        final Switch lockStatus = (Switch) convertView.findViewById(R.id.lockState);
        final Button popupMenuButton = (Button) convertView.findViewById(R.id.popup_lock_menu_button);

        // Set the UI elements up
        lockLabel.setText(lock.getLabel());

        // Sets the status message and color
        if (lock.getIsConnected()) {
            connectedStatus.setText("connected");
            connectedStatus.setTextColor(Color.parseColor(CONNECTED_COLOR));
        }
        else {
            connectedStatus.setText("disconnected");
            connectedStatus.setTextColor(Color.parseColor(DISCONNECTED_COLOR));
        }

        // Handle switch behavior
        lockStatus.setChecked(lock.getIsLocked());
        lockStatus.setEnabled(false);

        // Handle popup behavior
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof LockListScreen) {
                    ((LockListScreen)mContext).showPopUp(((LockListScreen)mContext).findViewById(R.id.gridView), lock);
                }
            }
        });

        // Handle tile behavior
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000 || !lock.getIsConnected()) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (mContext instanceof LockListScreen) {
                    lock.unlock((LockListScreen)mContext);
                    lockStatus.setChecked(lock.getIsLocked());
                }
            }
        });

        return convertView;
    }
}
