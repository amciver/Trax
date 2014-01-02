package com.experiment.trax;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import com.experiment.trax.core.BaseActivity;
import com.experiment.trax.fragments.DropsitesFragment;
import com.experiment.trax.listeners.SetDropsitesCompleteListener;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 2/17/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropsiteSelectionActivity extends BaseActivity {

    private boolean mIsRefreshing;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d("DropsiteSelectionActivity", "onCreate called");

        //this must be done before we add anything other UI component
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        Log.d("DropsiteSelectionActivity", "setContentView as R.layout.dropsite_selection");
        setContentView(R.layout.dropsite_selection);

        setRefreshState(true);

        DropsitesFragment fragment = (DropsitesFragment) getSupportFragmentManager().findFragmentById(R.id.dropsites_listing);
        fragment.setOnSetDropsitesCompleteListener(new SetDropsitesCompleteListener() {
            @Override
            public void onSetDropsitesCompleted() {
                setRefreshState(false);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_dropsite_selection).setVisible(!mIsRefreshing);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.dropsite_selection_menu, menu);
        return true;
    }

    private void setRefreshState(boolean refreshing) {
        mIsRefreshing = refreshing;
        supportInvalidateOptionsMenu();

        if (mIsRefreshing)
            setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        else
            setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
    }

    @Override
    protected void onAddTabs() {
        //leaving this blank as we do not want tabs here
    }

    public boolean onRefreshDropsites(final com.actionbarsherlock.view.MenuItem item) {
        Log.d("DropsiteSelectionActivity", "onRefreshDropsites called");
        setRefreshState(true);

        DropsitesFragment fragment = (DropsitesFragment) getSupportFragmentManager().findFragmentById(R.id.dropsites_listing);
        fragment.setOnSetDropsitesCompleteListener(new SetDropsitesCompleteListener() {
            @Override
            public void onSetDropsitesCompleted() {
                setRefreshState(false);
            }
        });
        Log.d("DropsiteSelectionActivity", "Calling setDropsites() on " + DropsitesFragment.class.toString());
        fragment.setDropsites();

        return true;
    }
}