package com.experiment.trax.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.experiment.trax.TraxApplication;
import com.experiment.trax.adapters.TreeAdapter;
import com.experiment.trax.listeners.GetInstagramPhotosCompleteListener;
import com.experiment.trax.services.IInstagramService;
import com.experiment.trax.services.ImplInstagramService;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 5/8/13
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PhotosFragment extends PullToRefreshListFragment implements PullToRefreshBase.OnRefreshListener<ListView> {

    private final String TAG = "christmastreefarm";

    private final String LOG_TAG = "PhotosFragment";
    private final String SETTINGS_PHOTO_POSITION = "currentPosition";

    private PhotosFragment mThis = null;

    private IInstagramService mInstagramService;
    private PullToRefreshListView mTreesWrapperListView;
    private TreeAdapter mTreesAdapter;
    private List<JSONObject> mTrees = new ArrayList<JSONObject>();

    private int mCurrentPosition = -1;

    @Override
    protected PullToRefreshListView onCreatePullToRefreshListView(LayoutInflater inflater, Bundle savedInstanceState) {
        mTreesWrapperListView = super.onCreatePullToRefreshListView(inflater, savedInstanceState);

        //store the reference so we can make use of it within our handler
        mThis = this;

        return mTreesWrapperListView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCurrentPosition = getCurrentPosition(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.getPullToRefreshListView().setMode(PullToRefreshBase.Mode.BOTH);

        mTreesWrapperListView.setOnRefreshListener(this);

        mTreesAdapter = new TreeAdapter(view.getContext(), android.R.layout.simple_list_item_1, mTrees);
        mTreesWrapperListView.getRefreshableView().setAdapter(mTreesAdapter);

        //ui setup, need hook in to underlying ListView, not wrapper
        //mTreesWrapperListView.getRefreshableView().setDivider(null);
        //mTreesWrapperListView.getRefreshableView().setDividerHeight(0);

        getPhotos();
    }

    private void getPhotos() {

        //loading imagery, show the progress bar
        mThis.setListShown(false);

        mInstagramService = new ImplInstagramService();
        mInstagramService.setOnGetInstagramPhotosCompleteListener(new GetInstagramPhotosCompleteListener() {
            @Override
            public void onPhotoFetchComplete(final JSONArray photos) {
                try {
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject item = (JSONObject) photos.get(i);
                        mTrees.add(mTrees.size(), item);
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }

                Log.d(LOG_TAG, "Trees size is [" + mTrees.size() + "]");
                Log.d(LOG_TAG, "Current position is [" + mCurrentPosition + "]");
                if (mTrees.size() >= mCurrentPosition) {

                    mTreesAdapter.notifyDataSetChanged();

                    //TODO: This works now, would like no visible "selection" when setting item
                    mTreesWrapperListView.getRefreshableView().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(LOG_TAG, "Posting current position as [" + mCurrentPosition + "]");
                            mTreesWrapperListView.getRefreshableView().setSelection(mCurrentPosition);
                        }
                    });

                    mTreesWrapperListView.onRefreshComplete();
                } else {
                    mInstagramService.getInstagramPhotosAsync(TAG);
                }

                //done loading imagery, hide progress bar
                mThis.setListShown(true);

            }
        });

        Log.d(LOG_TAG, "Making call to ImplInstagramService with tag [" + TAG + "]");
        mInstagramService.getInstagramPhotosAsync(TAG);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveCurrentPosition(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        saveCurrentPosition(null);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        Log.d(LOG_TAG, "Notification to refresh the listing of trees called");
        saveCurrentPosition(null);
        mInstagramService.getInstagramPhotosAsync(TAG);
    }

    private int getCurrentPosition(Bundle savedInstanceState) {
        int currentPosition = -1;

        Log.d(LOG_TAG, "Attempting to retrieve image position from Bundle [" + savedInstanceState + "]");
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(SETTINGS_PHOTO_POSITION);
            Log.d(LOG_TAG, "Image position [" + currentPosition + "] obtained from Bundle [" + savedInstanceState + "]");
        }

        if (currentPosition == -1) {
            Log.d(LOG_TAG, "Attempting to retrieve image position from SharedPreferences");
            currentPosition = getActivity().getSharedPreferences(((TraxApplication) getActivity().getApplication()).PREFS, 0).getInt(SETTINGS_PHOTO_POSITION, -1);
            Log.d(LOG_TAG, "Image position [" + currentPosition + "] obtained from SharedPreferences");
        }

        Log.d(LOG_TAG, "Returning image position [" + currentPosition + "]");
        return currentPosition;
    }

    private void saveCurrentPosition(Bundle outState) {
        mCurrentPosition = mTreesWrapperListView.getRefreshableView().getLastVisiblePosition();

        if (outState != null) {
            outState.putInt(SETTINGS_PHOTO_POSITION, mCurrentPosition);
        } else {
            String prefs = ((TraxApplication) getActivity().getApplication()).PREFS;
            Log.d(LOG_TAG, "Saving position [" + mCurrentPosition + "] to SharedPreferences [" + prefs + "]");

            SharedPreferences settings = getActivity().getSharedPreferences(prefs, 0);
            SharedPreferences.Editor editor = settings.edit();
            mCurrentPosition = mTreesWrapperListView.getRefreshableView().getLastVisiblePosition();
            editor.putInt(SETTINGS_PHOTO_POSITION, mCurrentPosition);
            editor.commit();
        }
    }
}
