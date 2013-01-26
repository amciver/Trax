package com.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.experiment.adapters.LotAdapter;
import com.experiment.fragments.LotsFragment;
import com.experiment.listeners.GetLocationsCompleteListener;
import com.experiment.models.Location;
import com.experiment.services.ImplSimpleDBService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/19/12
 * Time: 8:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class LotSelectionActivity extends SherlockFragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lot_selection);
        //showLots();
    }

    private void showLots() {

        //TODO: get users current location to pass in versus hard coded value

        ImplSimpleDBService srvc = new ImplSimpleDBService();
        srvc.setOnGetLocationCompleteListener(new GetLocationsCompleteListener() {
            @Override
            public void onLocationFetchComplete(final List<Location> locations) {

                LotsFragment lots = (LotsFragment) (getSupportFragmentManager().findFragmentById(R.id.lots_lots));
                LotAdapter adapter = new LotAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, locations);
                lots.setListAdapter(adapter);

                //we need to know when an item is clicked so we can provide the details
                SherlockListFragment listFragment = (SherlockListFragment) lots;
                listFragment.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //GoogleMap googleMap = ((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations.get(i).getPoint(), 13));

                        Intent intent = new Intent(getApplicationContext(), LotDetailsActivity.class);
                        startActivity(intent);

                        //FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                        //LotsFragment lotsFragment = (LotsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_lots));
                        //DetailsFragment detailsFragment = (DetailsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_details));
                        //trans.hide(lotsFragment);
                        //trans.show(detailsFragment);
                        //trans.commit();

                    }
                });

                GoogleMap googleMap = ((SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
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
                for (Location location : locations) {
                    Log.d("showLots", "Location " + location.getPointAsString() + " being added to map");

                    BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    if (position == 1)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green01);
                    if (position == 2)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green02);
                    if (position == 3)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green03);
                    if (position == 4)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green04);
                    if (position == 5)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green05);
                    if (position == 6)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green06);
                    if (position == 7)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green07);
                    if (position == 8)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green08);
                    if (position == 9)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green09);
                    if (position == 10)
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_green10);

                    addLotToMap(googleMap, icon, location);

                    position++;
                }

                //zoom in on the first one, which is closest to user
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0).getPoint(), 13));

            }
        });
        srvc.getLocationsAsync(this.getApplicationContext(), new LatLng(33.630182, -112.018622));
    }

    private void addLotToMap(GoogleMap googleMap, BitmapDescriptor icon, Location location) {
        googleMap.addMarker(new MarkerOptions()
                .position(location.getPoint())
                .title(location.getName())
                .snippet(location.getDescription())
                .icon(icon));
    }

//    private void init()
//    {
//        GoogleMap mMap;
//        Object d = getSupportFragmentManager().findFragmentById(R.id.map);
//        if(d == null)
//        {
//            Log.e("Yaddy", "We have nullllllll");
//        }
//        else
//
//        {
//        mMap = ((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map))).getMap();
//        if(mMap == null)
//            Log.e("Blah", "We have nulllllllllllllll");
//        else
//        {
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(33.4483771, -112.0740373))
//                .title("Hello world"));
//
//        }
//        }
//    }
}