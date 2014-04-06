package com.experiment.trax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.experiment.trax.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/24/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeAdapter extends ArrayAdapter<JSONObject> {

    private final String LOG_TAG = "TreeAdapter";

    private Context mContext;
    private List<JSONObject> mTrees;

    public TreeAdapter(Context context, int textViewResourceId, List<JSONObject> objects) {
        super(context, textViewResourceId, objects);

        mContext = context;
        mTrees = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tree, null);
        }

        //TODO: Put a uri to a missing image in case it fails
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        DateTime created = null;
        String location = "";
        String url = "";
        try {

            url = mTrees.get(position).getJSONObject("images").getJSONObject("standard_resolution").optString("url");
            Log.d(LOG_TAG, "URL for position [" + position + "] is [" + url + "]");

            String unix = mTrees.get(position).optString("created_time");
            created = new DateTime(Long.parseLong(unix) * 1000L);

            location = mTrees.get(position).optString("location");

            Log.d(LOG_TAG, "Location for position [" + position + "] is [" + location + "]");
            Log.d(LOG_TAG, "Time created for position [" + position + "] is [" + created.toString(formatter) + "]");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        //TODO: Will this blow up if the returned type is not a TextView?

        AQuery aq = new AQuery(convertView);
        ImageOptions options = new ImageOptions();
        options.memCache = false;
        options.fileCache = true;
        options.fallback = 0;
        options.ratio = 1.0f;
        options.animation = AQuery.FADE_IN_NETWORK;
        options.round = 10;

        aq.id(R.id.tree).progress(R.id.progress).image(url, options);

//        aq.id(R.id.tree).progress(R.id.progress).image(url, true, true, 0, 0, new BitmapAjaxCallback() {
//
//            @Override
//            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
//
//                iv.setImageBitmap(bm);
//
//                //do something to the bitmap
//                //iv.setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);
//
//            }
//        }).animate(AQuery.FADE_IN_NETWORK);


        TextView photoDate = (TextView) convertView.findViewById(R.id.photo_date);
        if (photoDate != null)
            photoDate.setText(created.toString(formatter));

        try {
            TextView photoLocation = (TextView) convertView.findViewById(R.id.photo_location);
            if (photoLocation != null && !location.equalsIgnoreCase("null")) {
                photoLocation.setText(mTrees.get(position).getJSONObject("location").optString("name"));
            } else {
                photoLocation.setText(R.string.empty);
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "", ex);
        }

        return convertView;
    }

}
