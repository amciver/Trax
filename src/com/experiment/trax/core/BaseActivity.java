package com.experiment.trax.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.util.AQUtility;
import com.experiment.trax.LotSelectionActivity;
import com.experiment.trax.R;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 4/11/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseActivity extends SherlockFragmentActivity {

    //This font should not include extension and should be a .ttf font placed
    //within assets/fonts folder
    final String FONT = "kayleigh";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpannableString s = new SpannableString(getApplicationContext().getString(R.string.app_name));
        s.setSpan(new com.experiment.trax.utils.TypefaceSpan(this, FONT), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onAddTabs();

        Log.d("BaseActivity", "onCreate called");
    }

    protected void onAddTabs() {
        Log.d("BaseActivity", "addTabs called");

        final ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab,
                                      FragmentTransaction ft) {
            }

            public void onTabUnselected(ActionBar.Tab tab,
                                        FragmentTransaction ft) {
            }

            public void onTabReselected(ActionBar.Tab tab,
                                        FragmentTransaction ft) {
            }
        };

        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.tab_title, null);
        //TextView photos = (TextView) customView.findViewById(R.id.action_custom_title);
        //photos.setText("Photos");
        actionBar.addTab(actionBar.newTab()
                .setText("Photos")
                .setTabListener(tabListener));
        //.setCustomView(photos));

        actionBar.addTab(actionBar.newTab()
                .setText("Types")
                .setTabListener(tabListener));
//
//        actionBar.addTab(actionBar.newTab()
//                .setText("youtube")
//                .setTabListener(tabListener));

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("BaseActivity", "onDestory called");

        //clean the file cache when root activity exit
        //the resulting total cache size will be less than 3M
        if (isTaskRoot()) {
            AQUtility.cleanCacheAsync(this);
        }
    }

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

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onShowLots(com.actionbarsherlock.view.MenuItem item) {
        Intent myIntent = new Intent(BaseActivity.this, LotSelectionActivity.class);
        startActivity(myIntent);
        return true;
    }

    public boolean onShowDropsites(com.actionbarsherlock.view.MenuItem item) {
        return false;
    }
}
