package com.experiment.trax.services;

import android.content.Context;
import android.location.Location;
import com.experiment.trax.listeners.GetLocalityCompleteListener;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 2/17/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ILocationService {

    void setApplicationContext(Context applicationContext);

    void setOnGetLocalityCompleteListener(GetLocalityCompleteListener listener);

    String getLocalitySync(double latitude, double longitude);

    void getLocalityAsync(double latitude, double longitude);

    Location getCurrentLocation();

}
