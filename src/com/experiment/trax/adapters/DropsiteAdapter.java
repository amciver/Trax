package com.experiment.trax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.experiment.trax.R;
import com.experiment.trax.models.DropsiteLocation;
import com.experiment.trax.utils.Convert;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/4/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropsiteAdapter extends ArrayAdapter<DropsiteLocation> {

    private final Map<String, Integer> mCityCodes;
    private final List<DropsiteLocation> mDropsites;
    private Context mContext;

    public DropsiteAdapter(Context context, int textViewResourceId, List<DropsiteLocation> objects) {
        super(context, textViewResourceId, objects);

        mContext = context;
        mDropsites = objects;


        mCityCodes = new HashMap<String, Integer>();
        mCityCodes.put("AZ-PHX", R.drawable.az_phoenix);
        mCityCodes.put("AZ-MESA", R.drawable.az_mesa);
        mCityCodes.put("AZ-GLENDALE", R.drawable.az_glendale);
        mCityCodes.put("AZ-GOODYEAR", R.drawable.az_goodyear);
        mCityCodes.put("AZ-SCOTTSDALE", R.drawable.az_scottsdale);
        mCityCodes.put("AZ-PEORIA", R.drawable.az_peoria);
        mCityCodes.put("AZ-CHANDLER", R.drawable.az_chandler);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("DropsiteAdapter", "getView called for position [" + position + "]");

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.dropsite, null);
        }
        clearOldLogo(v);
        DropsiteLocation dropsiteLocation = mDropsites.get(position);
        if (dropsiteLocation != null) {
            showLogo(v, dropsiteLocation.getCityCode());
            TextView name = (TextView) v.findViewById(R.id.dropsite_name);
            if (name != null)
                name.setText(dropsiteLocation.getName());


            showDates(v, dropsiteLocation);
        }
        return v;
    }

    private void showLogo(View v, String cityCode) {
        Integer id = mCityCodes.get(cityCode);
        if (id != null) {
            ImageView i = new ImageView(mContext);
            i.setImageResource(id);
            i.setLayoutParams(getImageLayout());
            LinearLayout parentLayout = (LinearLayout) v.findViewById(R.id.dropsite_parent_layout);
            if (parentLayout != null) {
                parentLayout.addView(i, 0); //set the image as the initial child in the view, displaying left
            }
        }
    }

    private LinearLayout.LayoutParams getImageLayout() {
        int px = Convert.getAsDIP(32, mContext.getResources());

        Log.d("DropsiteAdapter", "Setting logo size as [" + px + "x" + px + "] pixels");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        return params;
    }

    //TODO: Need to improve performance here if traversing entire tree becomes an issue
    private void clearOldLogo(View v) {
        for (int i = 0; i < ((ViewGroup) v).getChildCount(); ++i) {
            View nextChild = ((ViewGroup) v).getChildAt(i);
            if (nextChild instanceof ImageView)
                nextChild.setVisibility(View.GONE);
        }
    }

    private void showDates(View v, DropsiteLocation dropsiteLocation) {

        DateTime openDate = dropsiteLocation.getDateOpen();
        DateTime closeDate = dropsiteLocation.getDateClose();

        if (openDate != null && closeDate != null) {

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
}
