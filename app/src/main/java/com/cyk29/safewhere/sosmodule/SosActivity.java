package com.cyk29.safewhere.sosmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataObjects.User;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SosActivity extends AppCompatActivity {


    Button cancel;
    ImageView panic;

    ImageButton civilDefence, ambulance, fire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getUserLocation();

        cancel = findViewById(R.id.cancelBT);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        panic = findViewById(R.id.panic_modeIV);
        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.panic_fragment_container, new PanicmodeFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


        ambulance = findViewById(R.id.AmbulanceBT);
        civilDefence = findViewById(R.id.civilDefenceBT);
        fire = findViewById(R.id.firefighterBT);

        civilDefence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("991");
            }
        });

        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("999");
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("994");
            }
        });

    }

    public void call(String number){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            Toast.makeText(this, "No app available to handle the phone call.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmergencyAlert(User user){
        if (user != null) {
            String emergencyContactName = user.getEcName();
            String emergencyContactPhone = user.getEcPhone();
            String emergencyContactEmail = user.getEcEmail();

            String userLocation = getUserLocation();
            String subject = "Emergency Alert about your friend " + user.getName() + "!";
            String body = "Your friend " + user.getName() + " is in danger! Their location is: " + userLocation;

            sendSMS("+601164129497", body);

            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail(emergencyContactEmail, subject, body);

        }
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void alertContacts() {
        String uid = FirebaseAuth.getInstance().getUid();

        if (uid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        sendEmergencyAlert(currentUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }



    // Location

    Location loc;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private void requestLocationPermission() {
        // Check if the app has the necessary location permission
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }
    // Override onRequestPermissionsResult to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                // Permission granted, you can perform your location-related tasks here
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                // Permission denied, handle this situation (e.g., show a message to the user)
            }
        }
    }
    private void requestNewLocationFix() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult.getLastLocation() != null) {
                        loc = locationResult.getLastLocation();
                    }
                }
            }, null);
        }
    }
    private LocationRequest createLocationRequest() {
        return new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)  // 10 seconds in milliseconds
                .setFastestInterval(5000);  // 5 seconds in milliseconds
    }
    private String getGoogleMapsUrl(double latitude, double longitude) {
        return "https://www.google.com/maps/?q=" + latitude + "," + longitude;
    }

    private String getUserLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                loc = location;
                                String l = "https://www.google.com/maps/?q=" + location.getLatitude() + "," + location.getLongitude();
                                Log.d("Locationnn", l);
                            } else {
                                // No last known location available, request a new location fix
                                requestNewLocationFix();
                            }
                        }
                    });
        } else {
            requestLocationPermission();
        }
        if (loc != null) {
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
            return getGoogleMapsUrl(latitude, longitude);
        } else {
            return null;
        }
    }
}