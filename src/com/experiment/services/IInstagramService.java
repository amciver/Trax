package com.experiment.services;

import com.experiment.listeners.GetInstagramPhotosCompleteListener;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/25/13
 * Time: 10:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IInstagramService {

    void setOnGetInstagramPhotosCompleteListener(GetInstagramPhotosCompleteListener listener);

    void getInstagramPhotosAsync(String tag);
}
