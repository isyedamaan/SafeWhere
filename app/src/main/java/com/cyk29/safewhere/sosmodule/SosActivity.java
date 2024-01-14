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
import com.cyk29.safewhere.helperclasses.EmailHelper;
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

/**
 * This class represents the SOS activity in the SafeWhere application. It allows users to send SOS
 * alerts, call emergency contacts, and perform various SOS-related functions.
 */
public class SosActivity extends AppCompatActivity {
    private static final String TAG = "SosActivity";
    private User currentUser;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getUserLocation();
        getUserInfo();
        initializeUI();
    }

    /**
     * Initializes the user interface components and sets click listeners for buttons.
     */
    private void initializeUI(){
        Button cancel = findViewById(R.id.cancelBT);
        cancel.setOnClickListener(v -> finish());

        ImageView panic = findViewById(R.id.panic_modeIV);
        panic.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.panic_fragment_container, new PanicModeFragment())
                .addToBackStack(null)
                .commit());

        ImageButton callContacts = findViewById(R.id.call_contactsIB);
        callContacts.setOnClickListener(v -> call(currentUser.getEcPhone()));

        ImageButton ambulance = findViewById(R.id.AmbulanceBT);
        ImageButton civilDefence = findViewById(R.id.civilDefenceBT);
        ImageButton fire = findViewById(R.id.firefighterBT);

        civilDefence.setOnClickListener(v -> call("991"));
        ambulance.setOnClickListener(v -> call("999"));
        fire.setOnClickListener(v -> call("994"));
    }

    /**
     * Initiates a phone call to the specified phone number.
     *
     * @param number The phone number to call.
     */
    public void call(String number){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }

    /**
     * Loads an email template with placeholders replaced by specific values.
     *
     * @param context   The context of the application.
     * @param name      The user's name.
     * @param latitude  The latitude of the user's location.
     * @param longitude The longitude of the user's location.
     * @return The email content with placeholders replaced.
     */
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

    /**
     * Sends an SMS message to the specified phone number.
     *
     * @param phoneNumber The phone number to send the SMS to.
     * @param message     The SMS message content.
     */
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            Log.d(TAG, "sendSMS: "+e.getMessage());
        }
    }

    /**
     * Alerts emergency contacts by sending SMS messages and emails.
     */
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
            EmailHelper emailHelper = new EmailHelper();
            emailHelper.sendEmail(emergencyContactEmail, subject, htmlBody);
        }
    }

    /**
     * Retrieves user information from the Firebase Realtime Database.
     */
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

    /**
     * Requests a new location fix using the FusedLocationProviderClient.
     */
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
    /**
     * Creates a location request for high-accuracy location updates.
     *
     * @return The LocationRequest object.
     */
    private LocationRequest createLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setIntervalMillis(10000)
                .setMinUpdateIntervalMillis(2000)
                .build();
    }

    /**
     * Generates a Google Maps URL based on latitude and longitude.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The Google Maps URL.
     */
    private String getGoogleMapsUrl(double latitude, double longitude) {
        return "https://www.google.com/maps/?q=" + latitude + "," + longitude;
    }

    /**
     * Retrieves the user's location using the FusedLocationProviderClient.
     *
     * @return The user's location.
     */
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