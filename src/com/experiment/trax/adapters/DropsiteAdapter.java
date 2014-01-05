package com.experiment.trax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.experiment.trax.R;
import com.experiment.trax.models.DropsiteLocation;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/4/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropsiteAdapter extends ArrayAdapter<DropsiteLocation> {

    private List<DropsiteLocation> mDropsites;

    private Context mContext;

    public DropsiteAdapter(Context context, int textViewResourceId, List<DropsiteLocation> objects) {
        super(context, textViewResourceId, objects);

        mContext = context;
        mDropsites = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("DropsiteAdapter", "getView called for position [" + position + "]");

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.dropsite, null);
        }

        DropsiteLocation dropsiteLocation = mDropsites.get(position);
        if (dropsiteLocation != null) {
            TextView name = (TextView) v.findViewById(R.id.dropsite_name);
            if (name != null)
                name.setText(dropsiteLocation.getName());

            showDates(v, dropsiteLocation);
        }
        return v;
    }

    private void showDates(View v, DropsiteLocation dropsiteLocation) {

        DateTime openDate = dropsiteLocation.getDateOpen();
        DateTime closeDate = dropsiteLocation.getDateClose();

        boolean showOpenClose = ((Integer) openDate.compareTo(closeDate)).equals(0) ? false : true;

        TextView open = (TextView) v.findViewById(R.id.dropsite_hours_open);
        if (open != null) {
            open.setVisibility(View.VISIBLE);
            open.setText(openDate.toString("MMMM dd, Y"));
        }

        TextView seperator = (TextView) v.findViewById(R.id.dropsite_time_seperator);
        TextView close = (TextView) v.findViewById(R.id.dropsite_hours_close);

        if (showOpenClose) {
            if (seperator != null) {
                seperator.setVisibility(View.VISIBLE);
            }
            if (close != null) {
                close.setVisibility(View.VISIBLE);
                close.setText(closeDate.toString("MMMM dd, Y"));
            }
        } else {

            if (seperator != null) {
                seperator.setVisibility(View.GONE);
            }
            if (close != null) {

                close.setVisibility(View.GONE);
                close.setText("");
            }
        }
    }
}
