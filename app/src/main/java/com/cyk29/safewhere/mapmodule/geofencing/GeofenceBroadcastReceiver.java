package com.cyk29.safewhere.mapmodule.geofencing;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.NotificationItem;
import com.cyk29.safewhere.helperclasses.NotificationHelper;
import com.cyk29.safewhere.notificationmodule.NotificationMainActivity;
import com.cyk29.safewhere.helperclasses.EmailHelper;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * GeofenceBroadcastReceiver handles the geofence events and sends notifications, emails, and SMS alerts.
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Geofencing event error or null");
            return;
        }

        // Extracting information from the intent
        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("Email");
        String phone = intent.getStringExtra("Phone");

        handleGeofenceEvent(context, geofencingEvent, name, email, phone);
    }

    /**
     * Handles the geofencing event by sending alerts and notifications.
     */
    private void handleGeofenceEvent(Context context, GeofencingEvent geofencingEvent, String name, String email, String phone) {
        Date date = new Date(System.currentTimeMillis());
        sendNotification(context);
        sendEmailAndSms(context, geofencingEvent, name, email, phone, date);
        saveNotificationToFirebase(date);
    }

    /**
     * Sends a high priority notification.
     */
    private void sendNotification(Context context) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendHighPriorityNotification("SafeWhere", "You have exited the geofence, your contacts have been alerted.", "Geofencing", NotificationMainActivity.class);
    }

    /**
     * Sends an email and an SMS alert.
     */
    private void sendEmailAndSms(Context context, GeofencingEvent geofencingEvent, String name, String email, String phone, Date date) {
        Location location = geofencingEvent.getTriggeringLocation();
        if (location != null) {
            String geofencingEmailBody = loadEmailTemplateWithValues(context, name, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), date);
            EmailHelper emailHelper = new EmailHelper();
            emailHelper.sendEmail(email, "SafeWhere Geofencing Alert", geofencingEmailBody);
            sendSMS(phone, "SafeWhere Geofencing Alert: Your device " + name + " has exited the assigned geofence. Check your email for more details.");
        }
    }

    /**
     * Saves a notification item to Firebase.
     */
    private void saveNotificationToFirebase(Date date) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("notifications");
            String id = databaseReference.push().getKey();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            NotificationItem notificationItem = new NotificationItem(id, "Geofencing Alert", "You exited your geofence, your contacts were been alerted.", simpleDateFormat.format(date));
            if (id != null) {
                databaseReference.child(id).setValue(notificationItem);
            }
        } else {
            Log.d(TAG, "onReceive: UID is null");
        }
    }

    /**
     * Sends an SMS message.
     */
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            Log.e(TAG, "sendSMS: " + e.getMessage());
        }
    }

    /**
     * Loads and customizes the email template with the given values.
     */
    public static String loadEmailTemplateWithValues(Context context, String name, String latitude, String longitude, Date date) {
        String content = "";
        try {
            InputStream is = context.getResources().openRawResource(R.raw.geofencing_email_template);
            int size = is.available();
            byte[] buffer = new byte[size];
            int bytesRead = is.read(buffer);
            is.close();
            content = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            content = content.replace("[DeviceName]", name);
            content = content.replace("[Latitude]", latitude);
            content = content.replace("[Longitude]", longitude);
            content = content.replace("[AlertDate]", date.toString());
        } catch (Exception e) {
            Log.e(TAG, "loadEmailTemplateWithValues: " + e.getMessage());
        }
        return content;
    }
}