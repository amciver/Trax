package com.experiment.trax;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import com.experiment.trax.core.BaseActivity;
import com.experiment.trax.fragments.LotsFragment;
import com.experiment.trax.listeners.SetLotsCompleteListener;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 2/17/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotSelectionActivity extends BaseActivity {

    private boolean mIsRefreshing;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //this must be done before we add anything other UI component
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.lot_selection);

        setRefreshState(true);

        LotsFragment fragment = (LotsFragment) getSupportFragmentManager().findFragmentById(R.id.lots_listing);
        fragment.setOnSetLotsCompleteListener(new SetLotsCompleteListener() {
            @Override
            public void onSetLotsCompleted() {
                setRefreshState(false);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_lot_add).setVisible(!mIsRefreshing);
        menu.findItem(R.id.menu_lot_selection).setVisible(!mIsRefreshing);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.lot_selection_menu, menu);
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

    public boolean onRefreshLots(final com.actionbarsherlock.view.MenuItem item) {
        Log.d("LotSelectionActivity", "onRefreshLots called");
        setRefreshState(true);

        final LotsFragment fragment = (LotsFragment) getSupportFragmentManager().findFragmentById(R.id.lots_listing);
        fragment.setOnSetLotsCompleteListener(new SetLotsCompleteListener() {
            @Override
            public void onSetLotsCompleted() {
                //this will remove the listener
                fragment.removeOnSetLotsCompleteListener(this);
                setRefreshState(false);
            }
        });
        Log.d("LotSelectionActivity", "Calling setLots() on " + LotsFragment.class.toString());
        fragment.setLots();

        return true;
    }

    public boolean onAddLot(final com.actionbarsherlock.view.MenuItem item) {

        Log.d("LotSelectionActivity", "onAddLot called; calling " + LotAddActivity.class.toString());
        Intent lotAddIntent = new Intent(this.getApplicationContext(), LotAddActivity.class);
        startActivity(lotAddIntent);

        return true;
    }
}