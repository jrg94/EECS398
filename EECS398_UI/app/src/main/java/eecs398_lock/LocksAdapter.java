package eecs398_lock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
        return convertView;
    }
}
