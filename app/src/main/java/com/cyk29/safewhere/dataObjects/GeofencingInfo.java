package com.cyk29.safewhere.dataObjects;

import com.google.android.gms.maps.model.LatLng;

public class GeofencingInfo {

    private String name;
    private String alertNumber;
    private String alertEmail;
    private String geoPin;
    private String latitude;
    private String longitude;
    private int radius;

    // Required empty constructor for Firebase
    public GeofencingInfo() {
    }

    public GeofencingInfo(String name, String alertNumber, String alertEmail, String geoPin, LatLng location, int radius) {
        this.name = name;
        this.alertNumber = alertNumber;
        this.alertEmail = alertEmail;
        this.geoPin = geoPin;
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
}

