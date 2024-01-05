package com.cyk29.safewhere.mapmodule.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.GeofencingInfo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GeofencingOnActivity extends AppCompatActivity {
    private GeofencingInfo geofencingInfo;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private List<Geofence> geofenceList;

    String TAG = "MyGeofenceBroadcastReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing_on);
        geofencingInfo = (GeofencingInfo) getIntent().getSerializableExtra("geofencingInfo");

        geofenceList = new ArrayList<Geofence>();

        geofencingClient = LocationServices.getGeofencingClient(this);

        Geofence geofence = new Geofence.Builder()
                .setRequestId(geofencingInfo.getName())

                .setCircularRegion(
                        Double.parseDouble(geofencingInfo.getLatitude()),
                        Double.parseDouble(geofencingInfo.getLongitude()),
                        geofencingInfo.getRadius()
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        if(geofenceList==null){
            Toast.makeText(getApplicationContext(), "Geofences could not be added", Toast.LENGTH_SHORT).show();
        }
        geofenceList.add(geofence);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        1);
            }
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Geofences were added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Geofences could not be added", Toast.LENGTH_SHORT).show();
                        Log.e("GeofenceAddError", "Error adding geofences", e);
                    }
                });
    }

    private TextView messageTV, locationTV;
    private EditText disablePasswordET;
    private Button disableGeofencingBT;


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(getApplicationContext(), MyGeofenceBroadcastReceiver.class);
        // Determine mutability based on the SDK version
        int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                PendingIntent.FLAG_UPDATE_CURRENT;

        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, flag);
        return geofencePendingIntent;
    }


    private void handleDisableGeofencingBT() {
        disableGeofencingBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = disablePasswordET.getText().toString();
                if (password.equals(geofencingInfo.getGeoPin())) {
//                    disableGeofencing();
                } else {
                    disablePasswordET.setError("Incorrect password");
                }
            }
        });
    }

}