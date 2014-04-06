package com.experiment.trax.fragments;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockListFragment;
import com.experiment.trax.R;
import com.experiment.trax.adapters.TypeAdapter;
import com.experiment.trax.models.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 5/8/13
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TypesFragment extends SherlockListFragment {

    private final String LOG_TAG = "TypesFragment";

    List<Type> mTypes = new ArrayList<Type>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO Need to clean this up?
        if (savedInstanceState != null) {
            String blah = savedInstanceState.getString("Blah");
            Log.d(LOG_TAG, blah);
        }


        setTypes();

    }

    public void setTypes() {

        mTypes = createTypes();

        TypeAdapter adapter = new TypeAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mTypes);
        setListAdapter(adapter);
    }

    private List<Type> createTypes() {

        List<Type> treeTypes = new ArrayList<Type>();

        Type douglasFir = new Type();
        douglasFir.setId("1");
        douglasFir.setName(getString(R.string.types_content_douglar_fir_title));
        douglasFir.setDescription(getString(R.string.types_content_douglas_fir));


        Type balsamFir = new Type();
        balsamFir.setId("2");
        balsamFir.setName(getString(R.string.types_content_balsam_fir_title));
        balsamFir.setDescription(getString(R.string.types_content_balsam_fir));


        Type fraserFir = new Type();
        fraserFir.setId("3");
        fraserFir.setName(getString(R.string.types_content_fraser_fir_title));
        fraserFir.setDescription(getString(R.string.types_content_fraser_fir));


        Type whitePine = new Type();
        whitePine.setId("4");
        whitePine.setName(getString(R.string.types_content_white_pine_title));
        whitePine.setDescription(getString(R.string.types_content_white_pine));


        Type grandFir = new Type();
        grandFir.setId("5");
        grandFir.setName(getString(R.string.types_content_grand_fir_title));
        grandFir.setDescription(getString(R.string.types_content_grand_fir));

        Type nobleFir = new Type();
        nobleFir.setId("6");
        nobleFir.setName(getString(R.string.types_content_noble_fir_title));
        nobleFir.setDescription(getString(R.string.types_content_noble_fir));

        treeTypes.add(douglasFir);
        treeTypes.add(balsamFir);
        treeTypes.add(fraserFir);
        treeTypes.add(whitePine);
        treeTypes.add(grandFir);
        treeTypes.add(nobleFir);

        return treeTypes;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //TODO Need to clean this up?
        outState.putString("Blah", "BLAH BLAH");
    }
}
