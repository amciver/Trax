package com.experiment.trax.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.experiment.trax.R;
import com.experiment.trax.TraxApplication;
import com.experiment.trax.fragments.CareFragment;
import com.experiment.trax.fragments.PhotosFragment;
import com.experiment.trax.fragments.TypesFragment;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 5/8/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewsAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 3;
    private FragmentManager mFragmentManager = null;

    public ViewsAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    @Override
    public Fragment getItem(int position) {

        Fragment fragmentToReturn = null;
        switch (position) {
            case 0:
                fragmentToReturn = new PhotosFragment();
                break;
            case 1:
                fragmentToReturn = new TypesFragment();
                break;
            case 2:
                fragmentToReturn = new CareFragment();
                break;
        }
        return fragmentToReturn;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = "N/A";
        switch (position) {
            case 0:
                title = TraxApplication.INSTANCE.getTextAs(TraxApplication.INSTANCE.getText(R.string.tab_otannenbaum), "");
                break;
            case 1:
                title = TraxApplication.INSTANCE.getTextAs(TraxApplication.INSTANCE.getText(R.string.tab_types), "");
                break;
            case 2:
                title = TraxApplication.INSTANCE.getTextAs(TraxApplication.INSTANCE.getText(R.string.tab_care), "");
                break;
        }
        return title;
    }
}
