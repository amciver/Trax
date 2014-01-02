package com.experiment.trax.listeners;

import com.experiment.trax.models.DropsiteLocation;

import java.util.List;

/**
 * Created by amciver on 12/26/13.
 */
public interface GetDropsiteLocationsCompleteListener {

    void onDropsiteLocationFetchComplete(List<DropsiteLocation> dropsiteLocations);
}
