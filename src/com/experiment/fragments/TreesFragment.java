package com.experiment.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockListFragment;
import com.experiment.adapters.TreeAdapter;
import com.experiment.models.Location;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/22/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreesFragment extends SherlockListFragment {

    List<Location> mLocations = new ArrayList<Location>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTrees();
    }

//    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        Log.d("LotsFragment", "Create context menu");
//        MenuInflater inflater = getSherlockActivity().getSupportMenuInflater();
//        inflater.inflate(R.menu.lot_menu, contextMenu);
//    }

    public void setTrees() {
        //List<String> uris = new ArrayList<String>();
        //uris.add("http://images.free-extras.com/pics/w/wilson_football-953.jpg");
//        //uris.add("http://instagr.am/p/UwKDLNTFtb/media/?size=l");
//        //uris.add("http://instagr.am/p/UvcXdZFGql/media/?size=l");
//        //final Tree tree = new Tree();
//        //final List<Bitmap> images = new ArrayList<Bitmap>();
//        //ImageLoader.getInstance().loadImage(getActivity().getApplicationContext(), "http://images.free-extras.com/pics/w/wilson_football-953.jpg", new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(Bitmap loadedImage) {
//                images.add(loadedImage);
//
//                TreeAdapter adapter = new TreeAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, images);
//                setListAdapter(adapter);
//            }
//        });

        //List<Bitmap> images = new ArrayList<Bitmap>();

        List<JSONObject> items = new ArrayList<JSONObject>();
        JSONArray trees = getJSON();
        try {
            for (int i = 0; i < trees.length(); i++) {
                JSONObject item = (JSONObject) trees.get(i);
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("TreesFragment", "BOOM");
        }


        TreeAdapter adapter = new TreeAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);

    }

    final String CLIENT_ID = "f8bf37e4a94c4de392b43ebac820bda4";

    private JSONArray getJSON() {
        try {

            HttpClient client = new DefaultHttpClient();
            //HttpGet request = new HttpGet("http://api.twicsy.com/search/christmas+tree");

            HttpGet request = new HttpGet("https://api.instagram.com/v1/tags/christmastreefarm/media/recent?client_id=" + CLIENT_ID);
            HttpResponse response = client.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new IOException("Invalid response from server: " + status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            //read response into a buffered stream
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            //return result from buffered stream
            String dataAsString = new String(content.toByteArray());
            JSONObject jsonObject = new JSONObject(dataAsString);

            //JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            return jsonArray;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return null;
    }

    private class Tree {
        private String url;
        private Bitmap image;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }
    }
}
