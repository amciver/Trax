package com.experiment.trax;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.experiment.trax.adapters.ViewsAdapter;
import com.experiment.trax.core.BaseActivity;
import com.experiment.trax.services.ImplLocationService;

public class InitActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpServices();
        setContentView(R.layout.main);

        ViewsAdapter adapter = new ViewsAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
    }

    private void setUpServices() {
        ImplLocationService.INSTANCE.setApplicationContext(getApplicationContext());
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
    }

}