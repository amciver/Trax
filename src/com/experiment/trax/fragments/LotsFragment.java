package com.experiment.trax.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.experiment.trax.R;
import com.experiment.trax.adapters.LotAdapter;
import com.experiment.trax.listeners.GetLotLocationsCompleteListener;
import com.experiment.trax.listeners.SetLotsCompleteListener;
import com.experiment.trax.models.LotLocation;
import com.experiment.trax.services.ImplLocationService;
import com.experiment.trax.services.ImplSimpleDBService;
import com.google.android.gms.maps.CameraUpdate;
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

    private final String LOG_TAG = "LotsFragment";

    List<LotLocation> mLotLocations = new ArrayList<LotLocation>();

    List<SetLotsCompleteListener> mSetLotsCompleteListeners = new ArrayList<SetLotsCompleteListener>();

    public void setOnSetLotsCompleteListener(SetLotsCompleteListener listener) {
        this.mSetLotsCompleteListeners.add(listener);
        Log.d(LOG_TAG, "Listener added, [" + this.mSetLotsCompleteListeners + "] listeners exist");
    }

    //TODO: Verify that we dont hold on to a listener
    public void removeOnSetLotsCompleteListener(SetLotsCompleteListener listener) {
        boolean result = this.mSetLotsCompleteListeners.remove(listener);
        Log.d("LOG_TAG", "Result of removing listener was [" + result + "] - [" + this.mSetLotsCompleteListeners.size() + "] listeners remain");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(getListView());

        AdapterView listView = (AdapterView) getListView();
        listView.setLongClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GoogleMap googleMap = ((SupportMapFragment) (getActivity().getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(false);

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLotLocations.get(i).getPoint(), 13));
            }
        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("LotsFragment", "Clicked long");
//                return false;
//            }
//        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                LotLocation lotLocation = mLotLocations.get(info.position);

                android.view.MenuInflater inflater = getActivity().getMenuInflater();

                //if we dont have a phone number, return now
                if (lotLocation.getPhone() != null &&
                        lotLocation.getPhone().length() != 7 &&
                        lotLocation.getPhone().length() != 10)
                    inflater.inflate(R.menu.lot_menu_no_phone, contextMenu);
                else
                    inflater.inflate(R.menu.lot_menu, contextMenu);
            }
        });

        setLots();
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call_item: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                LotLocation lotLocation = mLotLocations.get(info.position);

                Intent action = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lotLocation.getPhone()));
                startActivity(action);
                return true;
            }
            case R.id.directions_item: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                LotLocation lotLocation = mLotLocations.get(info.position);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + ImplLocationService.INSTANCE.getCurrentLocation().getLatitude() + "," + ImplLocationService.INSTANCE.getCurrentLocation().getLongitude() + "&daddr=" + lotLocation.getPoint().latitude + "," + lotLocation.getPoint().longitude));
                startActivity(intent);
                return true;
            }
            case R.id.flag_item: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                LotLocation lotLocation = mLotLocations.get(info.position);

                ImplSimpleDBService srvc = new ImplSimpleDBService();
                srvc.addLotFlagAsync(getActivity().getApplicationContext(), lotLocation);
            }
            default:
                return super.onContextItemSelected(item);

        }
    }

    public void setLots() {

        android.location.Location currentLocation = ImplLocationService.INSTANCE.getCurrentLocation();

        //we will get null if there is no connection available and thus no location; airplane mode
        if (currentLocation != null) {
            final LatLng currentCoordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            ImplSimpleDBService srvc = new ImplSimpleDBService();
            srvc.setOnGetLocationCompleteListener(new GetLotLocationsCompleteListener() {
                @Override
                public void onLotLocationFetchComplete(List<LotLocation> lotLocations) {

                    //TODO: If this is a failure and we have no connection, we should notify the UI so it can pop a toast
                    mLotLocations = lotLocations;

                    LotAdapter adapter = new LotAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mLotLocations);
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
                    googleMap.clear();

                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(false);
                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            // Getting view from the layout file info_window_layout
                            View v = getLayoutInflater(null).inflate(R.layout.lot_info_window, null);

                            // Getting the position from the marker
                            LatLng latLng = marker.getPosition();

                            LotLocation lotLocation = null;
                            String markerId = marker.getId();
                            Log.d(LOG_TAG, "Lot location marker id [" + markerId + "]");
                            for (LotLocation l : mLotLocations)
                                if (l.getMarkerId().equals(markerId)) {
                                    lotLocation = l;
                                }
                            Log.d(LOG_TAG, "Lot location [" + lotLocation + "]");
                            if (lotLocation != null) {
                                TextView title = (TextView) v.findViewById(R.id.lot_info_title);
                                title.setText(lotLocation.getLocation());

                                TextView description = (TextView) v.findViewById(R.id.lot_info_description);
                                description.setText(lotLocation.getDescription());
                                Log.d(LOG_TAG, "Logging description " + lotLocation.getDescription());
                            }

                            return v;
                        }
                    });
                    CameraUpdate center = CameraUpdateFactory.newLatLng(currentCoordinates);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            String markerId = marker.getId();
                            Log.d(LOG_TAG, "Marker Id is [" + markerId + "]");
                        }
                    });

                    int position = 1;
                    for (LotLocation lotLocation : mLotLocations) {
                        Log.d("setLots", "Location " + lotLocation.getPointAsString() + " being added to map");

                        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        addLotToMap(googleMap, icon, lotLocation);

                        position++;
                    }

                    Log.d("setLots", "Notifying " + mSetLotsCompleteListeners.size() + " listeners");
                    //notify all listeners that work is complete
                    for (SetLotsCompleteListener listener : mSetLotsCompleteListeners) {
                        listener.onSetLotsCompleted();
                    }

                    //zoom in on the first one, which is closest to user
                    if (mLotLocations.size() > 0)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLotLocations.get(0).getPoint(), 13));
                }
            });

            srvc.getLocationsAsync(getActivity().getApplicationContext(), currentCoordinates);
        } else {
            Log.d("setLots", "Notifying " + mSetLotsCompleteListeners.size() + " listeners");
            //notify all listeners that work is complete
            for (SetLotsCompleteListener listener : mSetLotsCompleteListeners) {
                listener.onSetLotsCompleted();
            }
        }
    }

    private void addLotToMap(GoogleMap googleMap, BitmapDescriptor icon, LotLocation lotLocation) {

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(lotLocation.getPoint())
                .title(lotLocation.getLocation())
                .snippet(lotLocation.getDescription())
                .icon(icon));
        lotLocation.setMarkerId(marker.getId());
        Log.d(LOG_TAG, "Adding marker with id [" + marker.getId() + "]");
    }


//    public boolean onContextItemSelected(MenuItem aItem) {
//        Log.d("MakingCall", "Im in fragment");
//        //AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
//        return false;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("MakingCall", "Im in fragment, onOptionsItemSelected");
//        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
//    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        GoogleMap googleMap = ((SupportMapFragment) (getActivity().getSupportFragmentManager().findFragmentById(R.id.lots_map))).getMap();
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocations.get(position).getPoint(), 13));
//
////        Intent intent = new Intent(getActivity().getApplicationContext(), LotDetailsActivity.class);
////        startActivity(intent);
//
////        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
////        LotsFragment lotsFragment = (LotsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_lots));
////        DetailsFragment detailsFragment = (DetailsFragment)(getSupportFragmentManager().findFragmentById(R.id.lots_details));
////        trans.hide(lotsFragment);
////        trans.show(detailsFragment);
////        trans.commit();
//    }


}
