package com.experiment.trax.fragments;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockListFragment;
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
        douglasFir.setName("Douglas Fir");
        douglasFir.setDescription("Long soft blue-green needles and a sweet fragrance with flexible, medium weight branches.");


        Type balsamFir = new Type();
        balsamFir.setId("2");
        balsamFir.setName("Balsam Fir");
        balsamFir.setDescription("Medium length very soft needles with a slightly tapered shape, the tree is full and has strong branches and excellent needle retention. Very fragrant tree that exudes the scent of Christmas.");


        Type fraserFir = new Type();
        fraserFir.setId("3");
        fraserFir.setName("Fraser Fir");
        fraserFir.setDescription("Short, soft and rounded at the tip needles with a deep green on the topside of the needle and a silver tint underside. Standard fir fragrance with an extremely lengthy life after being cut, able to be put up throughout the entire season.");


        Type douglasFir4 = new Type();
        douglasFir4.setId("1");
        douglasFir4.setName("Douglas Fir");
        douglasFir4.setDescription("Douglas fir also known as Oregon pine, or Douglas spruce, is an evergreen conifer species native to western North America. The common name is misleading since it is not a true fir. For this reason the name is often written as Douglas-fir.");


        Type douglasFir5 = new Type();
        douglasFir5.setId("1");
        douglasFir5.setName("Douglas Fir");
        douglasFir5.setDescription("Douglas fir also known as Oregon pine, or Douglas spruce, is an evergreen conifer species native to western North America. The common name is misleading since it is not a true fir. For this reason the name is often written as Douglas-fir.");

        Type douglasFir6 = new Type();
        douglasFir6.setId("1");
        douglasFir6.setName("Douglas Fir");
        douglasFir6.setDescription("Douglas fir also known as Oregon pine, or Douglas spruce, is an evergreen conifer species native to western North America. The common name is misleading since it is not a true fir. For this reason the name is often written as Douglas-fir.");

        Type douglasFir7 = new Type();
        douglasFir7.setId("1");
        douglasFir7.setName("Douglas Fir");
        douglasFir7.setDescription("Douglas fir also known as Oregon pine, or Douglas spruce, is an evergreen conifer species native to western North America. The common name is misleading since it is not a true fir. For this reason the name is often written as Douglas-fir.");

        Type douglasFir8 = new Type();
        douglasFir8.setId("1");
        douglasFir8.setName("Douglas Fir");
        douglasFir8.setDescription("Douglas fir also known as Oregon pine, or Douglas spruce, is an evergreen conifer species native to western North America. The common name is misleading since it is not a true fir. For this reason the name is often written as Douglas-fir.");

        treeTypes.add(douglasFir);
        treeTypes.add(balsamFir);
        treeTypes.add(fraserFir);

        return treeTypes;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("Blah", "BLAH BLAH");
    }
}
