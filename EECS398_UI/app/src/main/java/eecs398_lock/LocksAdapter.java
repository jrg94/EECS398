package eecs398_lock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.christen.eecs398_ui.R;

import java.util.ArrayList;

/**
 * Created by JRG94 on 2/25/2016.
 */
public class LocksAdapter extends ArrayAdapter<SmartLock> {

    public LocksAdapter(Context context, ArrayList<SmartLock> locks) {
        super(context, R.layout.message, locks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Retrieve lock from position
        SmartLock lock = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message, parent, false);
        }

        TextView lockID = (TextView) convertView.findViewById(R.id.lockID);
        TextView lockLat = (TextView) convertView.findViewById(R.id.lockLat);
        TextView lockLong = (TextView) convertView.findViewById(R.id.lockLong);

        lockID.setText(String.format("%s: %d", "ID", lock.getID()));
        lockLat.setText(String.format("%s: %f", "Latitude", lock.getLocation().getLatitude()));
        lockLong.setText(String.format("%s: %f", "Longitude", lock.getLocation().getLongitude()));

        return convertView;
    }
}
