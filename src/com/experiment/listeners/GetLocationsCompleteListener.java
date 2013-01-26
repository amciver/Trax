package com.experiment.listeners;

import com.experiment.models.Location;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/3/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GetLocationsCompleteListener {

    void onLocationFetchComplete(List<Location> locations);
}
