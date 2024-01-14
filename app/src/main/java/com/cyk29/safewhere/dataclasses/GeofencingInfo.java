package com.cyk29.safewhere.dataclasses;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Represents a user's geofencing information in the system.
 */
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

    /**
     * Default constructor required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public GeofencingInfo() {
    }

    /**
     * Constructs a new GeofencingInfo with the specified attributes.
     *
     * @param name The name of the geofence.
     * @param alertNumber The phone number to alert.
     * @param alertEmail The email to alert.
     * @param geoPin The pin of the geofence.
     * @param placeName The name of the place.
     * @param location The location of the geofence.
     * @param radius The radius of the geofence.
     */
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

    /**
     * @return the name
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getName() {return name;}
    /**
     * @param name the name to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setName(String name) {this.name = name;}

    /**
     * @return the alertNumber
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getAlertNumber() {return alertNumber;}
    /**
     * @param alertNumber the alertNumber to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setAlertNumber(String alertNumber) {this.alertNumber = alertNumber;}

    /**
     * @return the alertEmail
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getAlertEmail() {return alertEmail;}
    /**
     * @param alertEmail the alertEmail to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setAlertEmail(String alertEmail) {this.alertEmail = alertEmail;}

    /**
     * @return the geoPin
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getGeoPin() {return geoPin;}
    /**
     * @param geoPin the geoPin to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setGeoPin(String geoPin) {this.geoPin = geoPin;}

    /**
     * @return the location
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getLatitude() {return latitude;}
    /**
     * @param latitude the latitude to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setLatitude(String latitude) {this.latitude = latitude;}

    /**
     * @return the location
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getLongitude() {return longitude;}
    /**
     * @param longitude the longitude to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setLongitude(String longitude) {this.longitude = longitude;}

    /**
     * @return the radius
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public int getRadius() {return radius;}
    /**
     * @param radius the radius to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setRadius(int radius) {this.radius = radius;}

    /**
     * @return the isOn
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public boolean isOn() {return isOn;}
    /**
     * @param on the boolean to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setOn(boolean on) {isOn = on;}

    /**
     * @return the placeName
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public String getPlaceName() {return placeName;}

    /**
     * @param placeName the placeName to set
     * required for calls to DataSnapshot.getValue(GeofencingInfo.class)
     */
    @SuppressWarnings("unused")
    public void setPlaceName(String placeName) {this.placeName = placeName;}
}

