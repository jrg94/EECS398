package eecs398_lock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import app.lock.bluetooth.smart_lock_app.LockListScreen;
import app.lock.bluetooth.smart_lock_app.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JRG94 on 2/25/2016.
 * Overrides the functionality of ArrayAdapter to provide
 * custom functionality for the way locks are displayed
 */
public class LocksAdapter extends BaseAdapter {

    // Debugging
    private static final String TAG = "LocksAdapter";
    private static final boolean D = true;

    // Fields //
    private Context mContext;

    private HashMap<String, SmartLock> locks = new HashMap<String, SmartLock>();
    private String[] keys;

    public LocksAdapter(Context context, HashMap<String, SmartLock> locks) {
        this.locks = locks;
        keys = this.locks.keySet().toArray(new String[locks.size()]);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return locks.size();
    }

    @Override
    public Object getItem(int position) {
        return locks.get(keys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // CRUCIAL
        keys = this.locks.keySet().toArray(new String[locks.size()]);

        // Retrieve lock from position
        final SmartLock lock = (SmartLock) getItem(position);
        final LockListScreen lls = (LockListScreen) mContext;

        // TODO: Try to connect to this lock
        // TODO: Display connected or not
        //lls.mLockService.connect(lock.getDevice());

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lock_ui, parent, false);
        }

        // Initialize UI elements
        TextView lockLabel = (TextView) convertView.findViewById(R.id.lockLabel);
        TextView connectedStatus = (TextView) convertView.findViewById(R.id.connectedStatus);
        Switch lockStatus = (Switch) convertView.findViewById(R.id.lockState);
        final Button popupMenuButton = (Button) convertView.findViewById(R.id.popup_lock_menu_button);

        // Set the UI elements up
        lockLabel.setText(lock.getLabel());
        connectedStatus.setText(lock.getIsConnected() ? "connected" : "disconnected");

        lockStatus.setChecked(lock.getIsLocked());
        lockStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lock.toggleLock((LockListScreen)mContext);
            }
        });

        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof LockListScreen) {
                    lls.showPopUp(lls.findViewById(R.id.gridView), lock);
                }
            }
        });

        return convertView;
    }
}
