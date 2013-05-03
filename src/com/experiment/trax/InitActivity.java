package com.experiment.trax;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import com.experiment.trax.adapters.TreeAdapter;
import com.experiment.trax.core.BaseActivity;
import com.experiment.trax.listeners.GetInstagramPhotosCompleteListener;
import com.experiment.trax.services.IInstagramService;
import com.experiment.trax.services.ImplInstagramService;
import com.experiment.trax.services.ImplLocationService;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InitActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView> {

    private IInstagramService mInstagramService;
    private PullToRefreshListFragment mTreesListFragment;
    private PullToRefreshListView mTreesWrapperListView;
    private TreeAdapter mTreesAdapter;
    private List<JSONObject> mTrees = new ArrayList<JSONObject>();

    private final String TAG = "christmastreefarm";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setUpServices();
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

    private void setUpServices() {
        ImplLocationService.INSTANCE.setApplicationContext(getApplicationContext());
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