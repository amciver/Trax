package com.experiment.trax.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/28/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotLocation {

    private String id;
    private float rating;
    private String business;
    private String location;
    private String description;
    private LatLng point;
    private String phone;
    private Hashtable<Integer, String> hoursOpen = new Hashtable<Integer, String>();
    private Hashtable<Integer, String> hoursClose = new Hashtable<Integer, String>();
    private boolean acceptsAmex = false;
    private boolean acceptsVisa = false;
    private boolean acceptsMastercard = false;
    private boolean acceptsDiscover = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningTime(int dayOfWeek) {
        return com.experiment.trax.utils.String.formatHours(hoursOpen.get(dayOfWeek));
    }

    public String getClosingTime(int dayOfWeek) {
        return com.experiment.trax.utils.String.formatHours(hoursClose.get(dayOfWeek));
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public Hashtable<Integer, String> getHoursClose() {
        return hoursClose;
    }

    public void setHoursClose(Hashtable<Integer, String> hoursClose) {
        this.hoursClose = hoursClose;
    }

    public Hashtable<Integer, String> getHoursOpen() {
        return hoursOpen;
    }

    public void setHoursOpen(Hashtable<Integer, String> hoursOpen) {
        this.hoursOpen = hoursOpen;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static LotLocation asLocation(android.location.Location location) {
        LotLocation l = new LotLocation();
        l.setPoint(new LatLng(location.getLatitude(), location.getLongitude()));
        return l;
    }
}
