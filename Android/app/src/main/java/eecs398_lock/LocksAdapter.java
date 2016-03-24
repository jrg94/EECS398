package eecs398_lock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import app.lock.bluetooth.smart_lock_app.LockListScreen;
import app.lock.bluetooth.smart_lock_app.R;

import java.util.ArrayList;

/**
 * Created by JRG94 on 2/25/2016.
 * Overrides the functionality of ArrayAdapter to provide
 * custom functionality for the way locks are displayed
 */
public class LocksAdapter extends ArrayAdapter<SmartLock> {

    // Debugging
    private static final String TAG = "LocksAdapter";
    private static final boolean D = true;

    // Fields //
    private Context mContext;

    public LocksAdapter(Context context, ArrayList<SmartLock> locks) {
        super(context, R.layout.lock_ui, locks);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Retrieve lock from position
        final SmartLock lock = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lock_ui, parent, false);
        }

        TextView lockLabel = (TextView) convertView.findViewById(R.id.lockLabel);
        TextView lockID = (TextView) convertView.findViewById(R.id.lockID);
        TextView lockLat = (TextView) convertView.findViewById(R.id.lockLat);
        TextView lockLong = (TextView) convertView.findViewById(R.id.lockLong);
        Button popupMenuButton = (Button) convertView.findViewById(R.id.popup_lock_menu_button);

        lockLabel.setText(String.format("%s: %s", "Label", lock.getLabel()));
        lockID.setText(String.format("%s: %s", "ID", lock.getID().toString()));
        lockLat.setText(String.format("%s: %f", "Latitude", lock.getLocation().getLatitude()));
        lockLong.setText(String.format("%s: %f", "Longitude", lock.getLocation().getLongitude()));
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof LockListScreen) {
                    LockListScreen lls = (LockListScreen)mContext;
                    lls.showPopUp(lls.findViewById(R.id.gridView), lock);
                    lls.sendMessage("Toggling Lock");
                }
            }
        });

        return convertView;
    }
}
