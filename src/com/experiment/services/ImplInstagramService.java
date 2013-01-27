package com.experiment.services;

import android.os.AsyncTask;
import android.util.Log;
import com.experiment.listeners.GetInstagramPhotosCompleteListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/25/13
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImplInstagramService implements IInstagramService {

    private final String CLIENT_ID = "f8bf37e4a94c4de392b43ebac820bda4";
    private Hashtable<String, String> mTagMapping = new Hashtable<String, String>();
    private JSONArray mResults = null;

    List<GetInstagramPhotosCompleteListener> mGetInstagramPhotosCompleteListeners = new ArrayList<GetInstagramPhotosCompleteListener>();

    @Override
    public void setOnGetInstagramPhotosCompleteListener(GetInstagramPhotosCompleteListener listener) {
        this.mGetInstagramPhotosCompleteListeners.add(listener);
    }

    @Override
    public void getInstagramPhotosAsync(String tag) {
        new GetInstagramPhotosTask().execute(tag);
    }

    private class GetInstagramPhotosTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray results = null;
            try {

                String tag = params[0];
                String endpoint = "https://api.instagram.com/v1/tags/" + tag + "/media/recent?client_id=" + CLIENT_ID;

                //if we have not seen this tag before add it, otherwise get the next_url
                if (mTagMapping.containsKey(tag))
                    endpoint = mTagMapping.get(tag);

                Log.d("ImplInstagramService", "Making HttpGet call to [" + endpoint + "]");
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(endpoint);
                HttpResponse response = client.execute(request);

                // Check if server response is valid
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != 200) {
                    throw new IOException("Invalid response from server [" + status.toString() + "]");
                }

                // Pull content stream from response
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                //TODO: We can check status code of JSON as well
                //return result from buffered stream
                String dataAsString = builder.toString();
                JSONObject jsonObject = new JSONObject(dataAsString);

                //grab data to return
                results = jsonObject.getJSONArray("data");

                Log.d("ImplInstagramService", "Storing endpoint [" + jsonObject.getJSONObject("pagination").optString("next_url") + "] for tag [" + tag + "]");
                mTagMapping.put(tag, jsonObject.getJSONObject("pagination").optString("next_url"));


            } catch (Exception e) {
                Log.e("ImplInstagramService", e.getMessage(), e);
            }

            return results;
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            //Log.d("GetInstagramPhotosTask", "Passing " + mGetLocationsCompleteListeners.size() + " listeners " + locations.size() + " locations");
            //notify all listeners that work is complete
            //TODO: hack to get passed null for the moment

            if (result == null)
                result = new JSONArray();
            for (GetInstagramPhotosCompleteListener listener : mGetInstagramPhotosCompleteListeners) {
                listener.onPhotoFetchComplete(result);
            }

            super.onPostExecute(result);
        }
    }

}
