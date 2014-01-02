package com.experiment.trax.models;

import com.google.android.gms.maps.model.LatLng;
import org.joda.time.DateTime;

/**
 * Created by amciver on 12/26/13.
 */
public class DropsiteLocation {

    private String id;
    private String name;
    private String description;
    private LatLng point;
    private DateTime dateOpen;
    private DateTime dateClose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getPoint() {
        return point;
    }

    public String getPointAsString() {
        return point.latitude + "," + point.longitude;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    public DateTime getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(DateTime dateOpen) {
        this.dateOpen = dateOpen;
    }

    public DateTime getDateClose() {
        return dateClose;
    }

    public void setDateClose(DateTime dateClose) {
        this.dateClose = dateClose;
    }
}
