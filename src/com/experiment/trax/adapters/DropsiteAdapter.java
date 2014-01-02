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

        TextView open = (TextView) v.findViewById(R.id.dropsite_hours_open);
        if (open != null) {
            open.setVisibility(View.VISIBLE);
            open.setText(dropsiteLocation.getDateOpen().toString("MMMM dd, Y"));
        }

        TextView close = (TextView) v.findViewById(R.id.dropsite_hours_close);
        if (close != null) {
            close.setVisibility(View.VISIBLE);
            close.setText(dropsiteLocation.getDateClose().toString("MMMM dd, Y"));
        }
    }
}
