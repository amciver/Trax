package com.experiment.trax.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/24/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeAdapter extends ArrayAdapter<JSONObject> {

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
            Log.d("TreeAdapter", "URL for position [" + position + "] is [" + url + "]");

            String unix = mTrees.get(position).optString("created_time");
            created = new DateTime(Long.parseLong(unix) * 1000L);

            location = mTrees.get(position).optString("location");

            Log.d("TreeAdapter", "Time created for position [" + position + "] is [" + created.toString(formatter) + "]");

        } catch (JSONException e) {
            Log.e("TreeAdapter", e.getMessage(), e);
        }

        //convertView.setBackgroundColor(R.color.abs__primary_text_holo_light);

        //TODO: Will this blow up if the returned type is not a TextView?


        AQuery aq = new AQuery(convertView);
        ImageOptions options = new ImageOptions();
        options.memCache = true;
        options.fileCache = true;
        options.fallback = 0;
        options.ratio = 1.0f;
        options.animation = AQuery.FADE_IN_NETWORK;
        options.round = 10;

        aq.id(R.id.tree).progress(R.id.progress).image(url, options);

        TextView photoDate = (TextView) convertView.findViewById(R.id.photo_date);
        if (photoDate != null)
            photoDate.setText(created.toString(formatter));

        TextView photoLocation = (TextView) convertView.findViewById(R.id.photo_location);
        if (photoLocation != null && location != null) {
            double latitude = 37.751851;
            double longitude = -122.426147;
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    String result = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
                    Log.d("ImplLocationService", "Storing location as [" + result + "]");
                }
            } catch (IOException e) {
                Log.e("ImplLocationService", e.getMessage(), e);
            }

            photoLocation.setText(location);
        }

        return convertView;

    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//        if (v == null) {
//            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = vi.inflate(R.layout.tree, null);
//        }
//
////        ImageLoader.getInstance().displayImage(mTrees.get(position), (ImageView)v);
////        final Tree tree = new Tree();
////        ImageLoader.getInstance().loadImage(mContext, mTrees.get(position), new SimpleImageLoadingListener() {
////            @Override
////            public void onLoadingComplete(Bitmap loadedImage) {
////                 tree.setImage(loadedImage);
////            }
////        });
//
//        ((ImageView)v).setImageBitmap(mTrees.get(position));
//
//        return v;
//    }

}
