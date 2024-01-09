package com.cyk29.safewhere.mapmodule.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.notificationmodule.NotificationHelper;
import com.cyk29.safewhere.notificationmodule.NotificationMainActivity;
import com.cyk29.safewhere.sosmodule.EmailSender;
import com.google.android.gms.location.GeofencingEvent;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Geofence triggered", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onReceive: hogaya");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("Email");
        String phone = intent.getStringExtra("Phone");
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendHighPriorityNotification("SafeWhere", "You have exited the geofence, your contacts have been alerted.", NotificationMainActivity.class);
        if(geofencingEvent == null){
            Log.d(TAG, "onReceive: null");
            return;
        }
        if(geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: error");
            return;
        }
        EmailSender emailSender = new EmailSender();
        Location location = geofencingEvent.getTriggeringLocation();
        if (location != null) {
            Log.d(TAG, "onReceive: location is null");
            return;
        }
        String geofencingEmailBody = loadEmailTemplateWithValues(context, name, String.valueOf(geofencingEvent.getTriggeringLocation().getLatitude()), String.valueOf(geofencingEvent.getTriggeringLocation().getLongitude()), date);
        emailSender.sendEmail(email, "SafeWhere Geofencing Alert", geofencingEmailBody);
        sendSMS(phone, "SafeWhere Geofencing Alert: Your device "+name+" has exited the assigned geofence, check your email for more details.");
    }
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            Log.d(TAG, "sendSMS: "+e.getMessage());
        }
    }

    public static String loadEmailTemplateWithValues(Context context, String name, String latitude, String longitude, Date date) {
        String content = "";
        try {
            InputStream is = context.getResources().openRawResource(R.raw.geofencing_email_template);
            int size = is.available();
            byte[] buffer = new byte[size];
            int bytesRead = is.read(buffer);
            is.close();
            content = new String(buffer,0,bytesRead, StandardCharsets.UTF_8);
            content = content.replace("[DeviceName]", name);
            content = content.replace("[Latitude]", latitude);
            content = content.replace("[Longitude]", longitude);
            content = content.replace("[AlertDate]", date.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}
