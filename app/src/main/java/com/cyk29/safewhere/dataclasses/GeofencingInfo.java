package com.cyk29.safewhere.dataclasses;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class GeofencingInfo implements Serializable {

    private String name;
    private String alertNumber;
    private String alertEmail;
    private String geoPin;
    private String placeName;
    private String latitude;
    private String longitude;
    private int radius;
    private boolean isOn;

    // Required empty constructor for Firebase
    @SuppressWarnings("unused")
    public GeofencingInfo() {
    }

    public GeofencingInfo(String name, String alertNumber, String alertEmail, String geoPin, String placeName, LatLng location, int radius) {
        this.name = name;
        this.alertNumber = alertNumber;
        this.alertEmail = alertEmail;
        this.geoPin = geoPin;
        this.placeName = placeName;
        this.latitude = String.valueOf(location.latitude);
        this.longitude = String.valueOf(location.longitude);
        this.radius = radius;
    }

    // Getters and Setters
    @SuppressWarnings("unused")
    public String getName() {return name;}
    @SuppressWarnings("unused")
    public void setName(String name) {this.name = name;}
    @SuppressWarnings("unused")
    public String getAlertNumber() {return alertNumber;}
    @SuppressWarnings("unused")
    public void setAlertNumber(String alertNumber) {this.alertNumber = alertNumber;}
    @SuppressWarnings("unused")
    public String getAlertEmail() {return alertEmail;}
    @SuppressWarnings("unused")
    public void setAlertEmail(String alertEmail) {this.alertEmail = alertEmail;}
    @SuppressWarnings("unused")
    public String getGeoPin() {return geoPin;}
    @SuppressWarnings("unused")
    public void setGeoPin(String geoPin) {this.geoPin = geoPin;}
    @SuppressWarnings("unused")
    public String getLatitude() {return latitude;}
    @SuppressWarnings("unused")
    public void setLatitude(String latitude) {this.latitude = latitude;}
    @SuppressWarnings("unused")
    public String getLongitude() {return longitude;}
    @SuppressWarnings("unused")
    public void setLongitude(String longitude) {this.longitude = longitude;}
    @SuppressWarnings("unused")
    public int getRadius() {return radius;}
    @SuppressWarnings("unused")
    public void setRadius(int radius) {this.radius = radius;}
    @SuppressWarnings("unused")
    public boolean isOn() {return isOn;}
    @SuppressWarnings("unused")
    public void setOn(boolean on) {isOn = on;}
    @SuppressWarnings("unused")
    public String getPlaceName() {return placeName;}
    @SuppressWarnings("unused")
    public void setPlaceName(String placeName) {this.placeName = placeName;}
}

