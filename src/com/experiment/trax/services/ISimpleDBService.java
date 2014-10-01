package com.experiment.trax.services;

import android.content.Context;
import com.experiment.trax.models.LotLocation;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/28/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISimpleDBService {

    void addLotLocationAsync(Context context, LotLocation location);

    void getLocationsAsync(Context context, LatLng coordinate);

    void getDropsitesAsync(Context context, LatLng coordinate);

    void loadLocationsAsync(String kml);
    ////void loadDropSitesAsync();
}
