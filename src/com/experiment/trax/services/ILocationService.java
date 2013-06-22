package com.experiment.trax.services;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 2/17/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ILocationService {

    void setApplicationContext(Context applicationContext);

    String getLocality(double latitude, double longitude);

}
