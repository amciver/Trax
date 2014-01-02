package com.experiment.trax.listeners;

import com.experiment.trax.models.LotLocation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/3/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GetLotLocationsCompleteListener {

    void onLotLocationFetchComplete(List<LotLocation> lotLocations);
}
