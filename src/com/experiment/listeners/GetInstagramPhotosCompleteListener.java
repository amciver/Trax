package com.experiment.listeners;

import org.json.JSONArray;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/25/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GetInstagramPhotosCompleteListener {
    void onPhotoFetchComplete(JSONArray photos);
}
