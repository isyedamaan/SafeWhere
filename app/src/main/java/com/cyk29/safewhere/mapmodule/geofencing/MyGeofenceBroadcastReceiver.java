package com.cyk29.safewhere.mapmodule.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyGeofenceBroadcastReceiver extends BroadcastReceiver {

    String TAG = "MyGeofenceBroadcastReceiver";
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent == null){
            Log.e(TAG, "geofencingEvent is null");
            return;
        }
        if ( geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT || geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Location location = geofencingEvent.getTriggeringLocation();
            Log.i(TAG, createExitMessage(location));
        } else {
            // Log the error.
            Log.e(TAG, "Error MyGeofenceBroadcastReceiver");
        }
    }

    private String createExitMessage(Location location) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());
        String message = "Exited geofence at " + currentTime;

        if (location != null) {
            String mapsLink = String.format(Locale.getDefault(), "https://www.google.com/maps/?q=%.6f,%.6f",
                    location.getLatitude(), location.getLongitude());
            message += " - Google Maps Location: " + mapsLink;
        }

        return message;
    }


}