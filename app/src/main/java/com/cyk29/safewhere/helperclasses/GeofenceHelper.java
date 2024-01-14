package com.cyk29.safewhere.helperclasses;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.cyk29.safewhere.mapmodule.geofencing.GeofenceBroadcastReceiver;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

/**
 * GeofenceHelper is a utility class to assist with geofencing operations.
 */
public class GeofenceHelper extends ContextWrapper {
    private PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    /**
     * Creates a GeofencingRequest for a specific Geofence.
     * @param geofence The geofence to be requested.
     * @return GeofencingRequest object.
     */
    public GeofencingRequest geofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .build();
    }

    /**
     * Creates a Geofence object with specified parameters.
     * @param ID The ID for the geofence.
     * @param latLng The location of the geofence.
     * @param radius The radius of the geofence.
     * @return A Geofence object.
     */
    public Geofence getGeofence(String ID, LatLng latLng, int radius) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    /**
     * Creates or retrieves a PendingIntent for Geofencing.
     * @param name The name to be passed in the intent.
     * @param email The email to be passed in the intent.
     * @param phone The phone number to be passed in the intent.
     * @return A PendingIntent for geofencing events.
     */
    public PendingIntent getPendingIntent(String name, String email, String phone) {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        intent.putExtra("Email", email);
        intent.putExtra("Name", name);
        intent.putExtra("Phone", phone);
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        return pendingIntent;
    }

    /**
     * Returns a human-readable error string for geofencing errors.
     * @param e The exception to analyze.
     * @return A string describing the error.
     */
    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            switch (((ApiException) e).getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
                default:
                    return e.getLocalizedMessage();
            }
        }
        return e.getLocalizedMessage();
    }
}
