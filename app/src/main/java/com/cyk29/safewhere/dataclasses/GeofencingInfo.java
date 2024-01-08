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
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getAlertNumber() {return alertNumber;}
    public void setAlertNumber(String alertNumber) {this.alertNumber = alertNumber;}
    public String getAlertEmail() {return alertEmail;}
    public void setAlertEmail(String alertEmail) {this.alertEmail = alertEmail;}
    public String getGeoPin() {return geoPin;}
    public void setGeoPin(String geoPin) {this.geoPin = geoPin;}
    public String getLatitude() {return latitude;}
    public void setLatitude(String latitude) {this.latitude = latitude;}
    public String getLongitude() {return longitude;}
    public void setLongitude(String longitude) {this.longitude = longitude;}
    public int getRadius() {return radius;}
    public void setRadius(int radius) {this.radius = radius;}
    public boolean isOn() {return isOn;}
    public void setOn(boolean on) {isOn = on;}
    public String getPlaceName() {return placeName;}
    public void setPlaceName(String placeName) {this.placeName = placeName;}
}
