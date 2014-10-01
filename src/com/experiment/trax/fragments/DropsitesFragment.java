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
import com.experiment.trax.adapters.DropsiteAdapter;
import com.experiment.trax.listeners.GetDropsiteLocationsCompleteListener;
import com.experiment.trax.listeners.SetDropsitesCompleteListener;
import com.experiment.trax.models.DropsiteLocation;
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
public class DropsitesFragment extends SherlockListFragment {

    List<DropsiteLocation> mDropsiteLocations = new ArrayList<DropsiteLocation>();

    List<SetDropsitesCompleteListener> mSetDropsitesCompleteListeners = new ArrayList<SetDropsitesCompleteListener>();

    public void setOnSetDropsitesCompleteListener(SetDropsitesCompleteListener listener) {
        this.mSetDropsitesCompleteListeners.add(listener);
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
                GoogleMap googleMap = ((SupportMapFragment) (getActivity().getSupportFragmentManager().findFragmentById(R.id.dropsites_map))).getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(false);

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDropsiteLocations.get(i).getPoint(), 13));
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                DropsiteLocation dropsiteLocation = mDropsiteLocations.get(info.position);

                contextMenu.setHeaderTitle(dropsiteLocation.getLocation());

                android.view.MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.dropsite_menu, contextMenu);
            }
        });

        setDropsites();
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.directions_item: {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                DropsiteLocation dropsiteLocation = mDropsiteLocations.get(info.position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + ImplLocationService.INSTANCE.getCurrentLocation().getLatitude() + "," + ImplLocationService.INSTANCE.getCurrentLocation().getLongitude() + "&daddr=" + dropsiteLocation.getPoint().latitude + "," + dropsiteLocation.getPoint().longitude));
                startActivity(intent);
                return true;
            }
            default:
                return super.onContextItemSelected(item);

        }
    }

    public void setDropsites() {

        android.location.Location currentLocation = ImplLocationService.INSTANCE.getCurrentLocation();

        //we will get null if there is no connection available and thus no location; airplane mode
        if (currentLocation != null) {
            final LatLng currentCoordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            ImplSimpleDBService srvc = new ImplSimpleDBService();
            srvc.setOnGetLocationCompleteListener(new GetDropsiteLocationsCompleteListener() {
                @Override
                public void onDropsiteLocationFetchComplete(List<DropsiteLocation> dropsiteLocations) {

                    mDropsiteLocations = dropsiteLocations;

                    if (mDropsiteLocations != null) {

                        DropsiteAdapter adapter = new DropsiteAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mDropsiteLocations);
                        setListAdapter(adapter);

                        GoogleMap googleMap = ((SupportMapFragment) (getActivity().getSupportFragmentManager().findFragmentById(R.id.dropsites_map))).getMap();
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
                                View v = getLayoutInflater(null).inflate(R.layout.dropsite_info_window, null);

                                // Getting the position from the marker
                                LatLng latLng = marker.getPosition();

                                DropsiteLocation dropsiteLocation = null;
                                String id = marker.getId();
                                for (DropsiteLocation l : mDropsiteLocations)
                                    if (l.getId().equals(id)) {
                                        dropsiteLocation = l;
                                    }

                                if (dropsiteLocation != null) {
                                    TextView title = (TextView) v.findViewById(R.id.dropsite_info_title);
                                    title.setText(dropsiteLocation.getLocation());

                                    TextView description = (TextView) v.findViewById(R.id.dropsite_info_description);
                                    description.setText(dropsiteLocation.getDescription());
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
                                String id = marker.getId();
                            }
                        });

                        int position = 1;
                        for (DropsiteLocation dropsiteLocation : mDropsiteLocations) {
                            Log.d("setDropsites", "Location " + dropsiteLocation.getPointAsString() + " being added to map");

                            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                            addDropsiteToMap(googleMap, icon, dropsiteLocation);

                            position++;
                        }

                        Log.d("setDropsites", "Notifying " + mSetDropsitesCompleteListeners.size() + " listeners");
                        //notify all listeners that work is complete
                        for (SetDropsitesCompleteListener listener : mSetDropsitesCompleteListeners) {
                            listener.onSetDropsitesCompleted();
                        }

                        //zoom in on the first one, which is closest to user
                        if (mDropsiteLocations.size() > 0)
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDropsiteLocations.get(0).getPoint(), 13));
                    }
                }
            });

            srvc.getDropsitesAsync(getActivity().getApplicationContext(), currentCoordinates);
        } else {
            Log.d("setDropsites", "Notifying " + mSetDropsitesCompleteListeners.size() + " listeners");
            //notify all listeners that work is complete
            for (SetDropsitesCompleteListener listener : mSetDropsitesCompleteListeners) {
                listener.onSetDropsitesCompleted();
            }
        }
    }

    private void addDropsiteToMap(GoogleMap googleMap, BitmapDescriptor icon, DropsiteLocation dropsiteLocation) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(dropsiteLocation.getPoint())
                .title(dropsiteLocation.getLocation())
                .snippet(dropsiteLocation.getDescription())
                .icon(icon));
        dropsiteLocation.setId(marker.getId());
    }
}
