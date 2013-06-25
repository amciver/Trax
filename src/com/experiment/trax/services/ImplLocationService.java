package com.experiment.trax.services;

import android.content.Context;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.experiment.trax.listeners.GetLocalityCompleteListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/31/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImplLocationService implements ILocationService {

    public final static ILocationService INSTANCE = new ImplLocationService();

    private Context mApplicationContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onProviderEnabled(String s) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onProviderDisabled(String s) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };
    private static Location mCurrentLocation;

    List<GetLocalityCompleteListener> mGetLocalityCompleteListeners = new ArrayList<GetLocalityCompleteListener>();

    @Override
    public void setOnGetLocalityCompleteListener(GetLocalityCompleteListener listener) {
        this.mGetLocalityCompleteListeners.add(listener);
    }


    private ImplLocationService() {

    }

    public void setApplicationContext(Context applicationContext) {
        mApplicationContext = applicationContext;
        mLocationManager = (LocationManager) mApplicationContext.getSystemService(Context.LOCATION_SERVICE);

        //grab the initial location so we do not return null out of the gates
        mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //register for updates so as the user moves we know where they are
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    public void getLocalityAsync(double latitude, double longitude) {
        new GetLocalityAsyncTask(latitude, longitude).execute();
    }

    public static Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public static Criteria createCoarseCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }

    public static Criteria createFineCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }

    public class GetLocalityAsyncTask extends AsyncTask<String, String, String> {
        double latitude;
        double longitude;

        public GetLocalityAsyncTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            Geocoder geocoder = new Geocoder(mApplicationContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    //TODO: Need to return true objects back, should not be hacking together string here
                    result = addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea();
                    Log.d("ImplLocationService", "Storing location as [" + result + "]");
                }
            } catch (IOException e) {
                Log.e("ImplLocationService", e.getMessage(), e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //notify all listeners that work is complete
            for (GetLocalityCompleteListener listener : mGetLocalityCompleteListeners) {
                listener.onGetLocalityComplete(result);
            }

            super.onPostExecute(result);
        }
    }
}
