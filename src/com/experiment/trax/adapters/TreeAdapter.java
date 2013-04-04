package com.experiment.trax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.experiment.trax.R;
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
        String url = "";
        try {
            JSONObject images = mTrees.get(position).getJSONObject("images").getJSONObject("low_resolution");
            url = images.optString("url");

        } catch (JSONException e) {
            Log.e("TreeAdapter", e.getMessage(), e);
        }

        //convertView.setBackgroundColor(R.color.abs__primary_text_holo_light);

        AQuery aq = new AQuery(convertView);

        ImageOptions options = new ImageOptions();
        options.memCache = true;
        options.fileCache = true;
        options.fallback = 0;
        options.ratio = 1.0f;
        options.animation = AQuery.FADE_IN_NETWORK;
        //options.round = 15;

        aq.id(R.id.tree).progress(R.id.progress).image(url, options);


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

//    private class Tree {
//        private String url;
//        private Bitmap image;
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public Bitmap getImage() {
//            return image;
//        }
//
//        public void setImage(Bitmap image) {
//            this.image = image;
//        }
//    }
}
