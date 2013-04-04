package com.experiment.trax;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.util.AQUtility;
import com.experiment.trax.adapters.TreeAdapter;
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

        setUpServices();
        setContentView(R.layout.main);

        //TODO: this will provide a different font but currently not vertically centered
        String font = "billabong";
        SpannableString s = new SpannableString("O Tannenbaum");
        s.setSpan(new com.experiment.trax.utils.TypefaceSpan(this, font), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

//        final ActionBar actionBar = getSupportActionBar();
//
//        // Specify that tabs should be displayed in the action bar.
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Create a tab listener that is called when the user changes tabs.
//        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//            public void onTabSelected(ActionBar.Tab tab,
//                                      FragmentTransaction ft) {
//            }
//
//            public void onTabUnselected(ActionBar.Tab tab,
//                                        FragmentTransaction ft) {
//            }
//
//            public void onTabReselected(ActionBar.Tab tab,
//                                        FragmentTransaction ft) {
//            }
//        };
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View customView = inflater.inflate(R.layout.tab_title, null);
//        TextView instagram = (TextView) customView.findViewById(R.id.action_custom_title);
//        instagram.setText("Instagram");
//        actionBar.addTab(actionBar.newTab()
//                .setTabListener(tabListener)
//                .setCustomView(instagram));
//
//        actionBar.addTab(actionBar.newTab()
//                .setText("twitter")
//                .setTabListener(tabListener));
//
//        actionBar.addTab(actionBar.newTab()
//                .setText("youtube")
//                .setTabListener(tabListener));
//
//        for(int i = 0; i<actionBar.getTabCount(); i++){
//            LayoutInflater inflater = LayoutInflater.from(this);
//            View customView = inflater.inflate(R.layout.tab_title, null);
//
//            TextView titleTV = (TextView) customView.findViewById(R.id.action_custom_title);
//            titleTV.setText(tabNames[i]);
//            //Here you can also add any other styling you want.
//
//            bar.getTabAt(i).setCustomView(customView);
//        }

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