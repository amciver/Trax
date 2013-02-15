package com.experiment.trax.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/28/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Location {

    private String name;
    private String business;
    private String description;
    private LatLng point;
    private String phone;
    private boolean acceptsAmex = false;
    private boolean acceptsVisa = false;
    private boolean acceptsMastercard = false;
    private boolean acceptsDiscover = false;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAcceptsAmex() {
        return acceptsAmex;
    }

    public void setAcceptsAmex(boolean acceptsAmex) {
        this.acceptsAmex = acceptsAmex;
    }

    public boolean isAcceptsVisa() {
        return acceptsVisa;
    }

    public void setAcceptsVisa(boolean acceptsVisa) {
        this.acceptsVisa = acceptsVisa;
    }

    public boolean isAcceptsMastercard() {
        return acceptsMastercard;
    }

    public void setAcceptsMastercard(boolean acceptsMastercard) {
        this.acceptsMastercard = acceptsMastercard;
    }

    public boolean isAcceptsDiscover() {
        return acceptsDiscover;
    }

    public void setAcceptsDiscover(boolean acceptsDiscover) {
        this.acceptsDiscover = acceptsDiscover;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Location asLocation(android.location.Location location) {
        Location l = new Location();
        l.setPoint(new LatLng(location.getLatitude(), location.getLongitude()));
        return l;
    }
}
