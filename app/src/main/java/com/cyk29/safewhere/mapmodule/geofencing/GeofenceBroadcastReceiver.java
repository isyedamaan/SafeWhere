package com.cyk29.safewhere.mapmodule.geofencing;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.cyk29.safewhere.dataclasses.GeofencingInfo;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.cyk29.safewhere.notificationmodule.NotificationHelper;
import com.cyk29.safewhere.notificationmodule.NotificationMainActivity;
import com.cyk29.safewhere.sosmodule.EmailSender;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiver";
    private String email;
    private String name;

    public static final String GEOFENCE_TRIGGERED = "com.cyk29.safewhere.GEOFENCE_TRIGGERED";


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Geofence triggered", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onReceive: hogaya");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        name = intent.getStringExtra("Name");
        email = intent.getStringExtra("Email");
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendHighPriorityNotification("SafeWhere", "You have exited the geofence, your contacts have been alerted.", NotificationMainActivity.class);
        EmailSender emailSender = new EmailSender();
        String geofencingEmailBody = "Your device named \""+name+"\" registered for geofencing with SafeWhere has exited the geofencing area. The alert was caused on "+date.toString()+".";
        emailSender.sendEmail("amaan9927@gmail.com", "SafeWhere Geofencing Alert", geofencingEmailBody);
    }

}
