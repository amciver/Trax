package com.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.util.AQUtility;
import com.experiment.adapters.TreeAdapter;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import java.util.LinkedList;
import java.util.List;

public class InitActivity extends SherlockFragmentActivity implements PullToRefreshBase.OnRefreshListener<ListView> {
    //private Resources resources;

    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    private PullToRefreshListFragment mPullRefreshListFragment;
    private PullToRefreshListView mPullRefreshListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setTrees();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        //clean the file cache when root activity exit
        //the resulting total cache size will be less than 3M
        if (isTaskRoot()) {
            AQUtility.cleanCacheAsync(this);
        }
    }

    //TODO: This needs to be in a base class for all the activities
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

//        MenuItem item = menu.add("Testing");
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return false;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });

        return true;
    }

    public boolean onShowLots(com.actionbarsherlock.view.MenuItem item) {
        //Intent intent = new Intent(InitActivity.this, LotsFragment.class);
        //startActivity(intent);
        Intent myIntent = new Intent(InitActivity.this, LotSelectionActivity.class);
        startActivity(myIntent);
        return true;
    }

    public boolean onShowFarms(com.actionbarsherlock.view.MenuItem item) {
        return false;
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
    }

    public void onClick(View v) {
        Intent myIntent = new Intent(InitActivity.this, LotSelectionActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        Log.d("InitActivity", "Notification to refresh the listing of trees called");
    }

    public void setTrees() {
        mPullRefreshListFragment = (PullToRefreshListFragment) getSupportFragmentManager().findFragmentById(
                R.id.trees);

        // Get PullToRefreshListView from Fragment
        mPullRefreshListView = mPullRefreshListFragment.getPullToRefreshListView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(this);

        // You can also just use mPullRefreshListFragment.getListView()
        ListView actualListView = mPullRefreshListView.getRefreshableView();


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


        //mListItems = new LinkedList<String>();
        //mListItems.addAll(Arrays.asList(mStrings));
        //mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        TreeAdapter adapter = new TreeAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        actualListView.setAdapter(adapter);

        mPullRefreshListFragment.setListShown(true);


        //setListAdapter(adapter);

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
}
