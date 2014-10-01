package com.experiment.trax.models;

import com.google.android.gms.maps.model.LatLng;
import org.joda.time.DateTime;

/**
 * Created by amciver on 12/26/13.
 */
public class DropsiteLocation {

    private String id;
    private String location;
    private String description;
    private LatLng point;
    private String cityCode;
    private DateTime dateOpen;
    private DateTime dateClose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
