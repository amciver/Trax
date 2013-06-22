package com.experiment.trax.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

    private static final int NUM_ITEMS = 2;

    public ViewsAdapter(FragmentManager fm) {
        super(fm);
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
        }
        return fragmentToReturn;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "N/A";
        switch (position) {
            case 0:
                title = "PHOTOS";
                break;
            case 1:
                title = "TYPES";
                break;
        }
        return title;
    }
}
