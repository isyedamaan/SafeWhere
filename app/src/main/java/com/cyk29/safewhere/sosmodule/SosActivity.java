package com.cyk29.safewhere.sosmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SosActivity extends AppCompatActivity {
    private static final String TAG = "SosActivity";
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getUserLocation();
        getUserInfo();
        initializeUI();
    }
    private void initializeUI(){
        // Cancel Button
        Button cancel = findViewById(R.id.cancelBT);
        cancel.setOnClickListener(v -> finish());
        // Panic Button
        ImageView panic = findViewById(R.id.panic_modeIV);
        panic.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.panic_fragment_container, new PanicmodeFragment())
                .addToBackStack(null)
                .commit());
        // Call Contacts Button
        ImageButton callContacts = findViewById(R.id.call_contactsIB);
        callContacts.setOnClickListener(v -> call(currentUser.getEcPhone()));
        // Alert Authorities Buttons
        ImageButton ambulance = findViewById(R.id.AmbulanceBT);
        ImageButton civilDefence = findViewById(R.id.civilDefenceBT);
        ImageButton fire = findViewById(R.id.firefighterBT);
        civilDefence.setOnClickListener(v -> call("991"));
        ambulance.setOnClickListener(v -> call("999"));
        fire.setOnClickListener(v -> call("994"));
    }
    public void call(String number){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }
    public static String loadEmailTemplateWithValues(Context context, String name, String latitude, String longitude) {
        String content = "";
        try {
            InputStream is = context.getResources().openRawResource(R.raw.sos_email_template);
            int size = is.available();
            byte[] buffer = new byte[size];
            int bytesRead = is.read(buffer);
            is.close();
            content = new String(buffer,0,bytesRead, StandardCharsets.UTF_8);
            content = content.replace("[Name]", name);
            content = content.replace("[Latitude]", latitude);
            content = content.replace("[Longitude]", longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            Log.d(TAG, "sendSMS: "+e.getMessage());
        }
    }
    public void alertContacts() {
        getUserInfo();
        if (currentUser != null) {
            String emergencyContactName = currentUser.getEcName();
            String emergencyContactPhone = currentUser.getEcPhone();
            String emergencyContactEmail = currentUser.getEcEmail();
            Location userLocation = getUserLocation();
            String subject = "Emergency Alert about your friend " + currentUser.getName() + "!";
            String body = "Dear "+emergencyContactName+",\nYour friend " + currentUser.getName() + " may be in danger, as they initiated SOS in their SafeWhere App! Their last known location is: " ;
            String htmlBody;
            if (userLocation != null) {
                body += getGoogleMapsUrl(userLocation.getLatitude(), userLocation.getLongitude())+".";
                htmlBody = loadEmailTemplateWithValues(this, currentUser.getName(), Double.toString(userLocation.getLatitude()), Double.toString(userLocation.getLongitude()));
            } else {
                body += "Unknown.";
                htmlBody = loadEmailTemplateWithValues(this, currentUser.getName(), null, null);
            }
            sendSMS(emergencyContactPhone, body);
            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail(emergencyContactEmail, subject, htmlBody);
        }
    }
    private void getUserInfo(){
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private void requestNewLocationFix() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult.getLastLocation() != null) {
                        userLocation = locationResult.getLastLocation();
                    }
                }
            }, null);
        }
    }
    private LocationRequest createLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setIntervalMillis(10000)
                .setMinUpdateIntervalMillis(2000)
                .build();
    }
    private String getGoogleMapsUrl(double latitude, double longitude) {
        return "https://www.google.com/maps/?q=" + latitude + "," + longitude;
    }
    private Location getUserLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            userLocation = location;
                        } else {
                            requestNewLocationFix();
                        }
                    });
        }
        if (userLocation != null) {
            return userLocation;
        } else {
            return null;
        }
    }
}