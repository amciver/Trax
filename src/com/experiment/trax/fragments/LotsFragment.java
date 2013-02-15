package com.experiment.trax.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.experiment.R;
import com.experiment.trax.adapters.LotAdapter;
import com.experiment.trax.listeners.GetLocationsCompleteListener;
import com.experiment.trax.models.Location;
import com.experiment.trax.services.ImplSimpleDBService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/4/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotsFragment extends SherlockListFragment {

    List<Location> mLocations = new ArrayList<Location>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(getListView());

        AdapterView listView = (AdapterView) getListView();
        listView.setLongClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("LotsFragment", "Clicked short");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("LotsFragment", "Clicked long");
                return false;
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;

                Location location = mLocations.get(info.position);
                contextMenu.setHeaderTitle(location.getName());

                android.view.MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.lot_menu, contextMenu);
            }
        });

        //empty class as we will populate this with an adapter once we fetch locations in LotSelectionActivity
        setLots();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);
    }

//    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        Log.d("LotsFragment", "Create context menu");
//        MenuInflater inflater = getSherlockActivity().getSupportMenuInflater();
//        inflater.inflate(R.menu.lot_menu, contextMenu);
//    }

    public void setLots() {
        ImplSimpleDBService srvc = new ImplSimpleDBService();
        srvc.setOnGetLocationCompleteListener(new GetLocationsCompleteListener() {
            @Override
            public void onLocationFetchComplete(List<Location> locations) {

                mLocations = locations;

                LotAdapter adapter = new LotAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mLocations);
                setListAdapter(adapter);

                //we need to know when an item is clicked so we can provide the details
//                SherlockListFragment listFragment = (SherlockListFragment)lots;
//                listFragment.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        //GoogleMap googleMap = ((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
//                        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations.get(i).getPoint(), 13));
//
//                        Intent intent = new Intent(getActivity().getApplicationContext(), LotDetailsActivity.class);
//                        startActivity(intent);
//
//                        //FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//                        //LotsFragment lotsFragment = (LotsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_lots));
//                        //DetailsFragment detailsFragment = (DetailsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_details));
//                        //trans.hide(lotsFragment);
//                        //trans.show(detailsFragment);
//                        //trans.commit();
//
//                    }
//                });

                GoogleMap googleMap = ((SupportMapFragment) (getActivity().getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.630182, -112.018622), 10));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String id = marker.getId();
                    }
                });

                int position = 1;
                for (Location location : mLocations) {
                    Log.d("showLots", "Location " + location.getPointAsString() + " being added to map");

                    BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    addLotToMap(googleMap, icon, location);

                    position++;
                }

                //zoom in on the first one, which is closest to user
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocations.get(0).getPoint(), 13));

            }
        });
        srvc.getLocationsAsync(getActivity().getApplicationContext(), new LatLng(33.630182, -112.018622));
    }

    private void addLotToMap(GoogleMap googleMap, BitmapDescriptor icon, Location location) {
        googleMap.addMarker(new MarkerOptions()
                .position(location.getPoint())
                .title(location.getName())
                .snippet(location.getDescription())
                .icon(icon));
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        //GoogleMap googleMap = ((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
//        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations.get(i).getPoint(), 13));
//
//
//        Intent intent = new Intent(getActivity().getApplicationContext(), LotDetailsActivity.class);
//        startActivity(intent);
//
//        //FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        //LotsFragment lotsFragment = (LotsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_lots));
//        //DetailsFragment detailsFragment = (DetailsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_details));
//        //trans.hide(lotsFragment);
//        //trans.show(detailsFragment);
//        //trans.commit();
//    }


}
