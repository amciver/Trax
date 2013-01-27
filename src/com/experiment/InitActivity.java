package com.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.util.AQUtility;
import com.experiment.adapters.TreeAdapter;
import com.experiment.listeners.GetInstagramPhotosCompleteListener;
import com.experiment.services.IInstagramService;
import com.experiment.services.ImplInstagramService;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InitActivity extends SherlockFragmentActivity implements PullToRefreshBase.OnRefreshListener<ListView> {
    //private Resources resources;

    private IInstagramService mInstagramService;

    private PullToRefreshListFragment mTreesListFragment;
    private PullToRefreshListView mTreesWrapperListView;
    private TreeAdapter mTreesAdapter;
    private List<JSONObject> mTrees = new ArrayList<JSONObject>();

    private final String TAG = "christmastreefarm";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("InitActivity", "onCreate called");
        setContentView(R.layout.main);

        //grab the PullToRefreshListFragment, grab the wrapper ListView and set refresh handler
        mTreesListFragment = (PullToRefreshListFragment) getSupportFragmentManager().findFragmentById(R.id.trees);
        mTreesWrapperListView = mTreesListFragment.getPullToRefreshListView();
        mTreesWrapperListView.setOnRefreshListener(this);

        mTreesAdapter = new TreeAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, mTrees);
        mTreesWrapperListView.getRefreshableView().setAdapter(mTreesAdapter);

        //ui setup, need hook in to underlying ListView, not wrapper
        mTreesWrapperListView.getRefreshableView().setDivider(null);
        mTreesWrapperListView.getRefreshableView().setDividerHeight(0);

        mTreesListFragment.setListShown(true);

        //set up instagram service and perform logic first time through to set adapter
        mInstagramService = new ImplInstagramService();
        mInstagramService.setOnGetInstagramPhotosCompleteListener(new GetInstagramPhotosCompleteListener() {
            @Override
            public void onPhotoFetchComplete(final JSONArray photos) {
                try {
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject item = (JSONObject) photos.get(i);
                        mTrees.add(0, item);
                    }
                } catch (JSONException e) {
                    Log.e("InitActivity", e.getMessage(), e);
                }

                mTreesAdapter.notifyDataSetChanged();

                //TODO: This works now, would like no visible "selection" when setting item
                mTreesWrapperListView.getRefreshableView().post(new Runnable() {
                    @Override
                    public void run() {
                        mTreesWrapperListView.getRefreshableView().setSelection(photos.length());
                    }
                });

                mTreesWrapperListView.onRefreshComplete();
            }
        });

        mInstagramService.getInstagramPhotosAsync(TAG);
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
//        savedInstanceState.putBoolean("MyBoolean", true);
//        savedInstanceState.putDouble("myDouble", 1.9);
//        savedInstanceState.putInt("MyInt", 1);
//        savedInstanceState.putString("MyString", "Welcome back to Android");
//    }

    @Override
    protected void onDestroy() {
        Log.d("InitiActivity", "onDestory called");
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
        Intent myIntent = new Intent(InitActivity.this, LotSelectionActivity.class);
        startActivity(myIntent);
        return true;
    }

    public boolean onShowDropsites(com.actionbarsherlock.view.MenuItem item) {
        return false;
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        Log.d("InitActivity", "Notification to refresh the listing of trees called");
        mInstagramService.getInstagramPhotosAsync(TAG);

    }
}