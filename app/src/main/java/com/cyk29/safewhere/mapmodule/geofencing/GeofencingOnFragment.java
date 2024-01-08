package com.cyk29.safewhere.mapmodule.geofencing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.GeofencingInfo;
import com.cyk29.safewhere.mapmodule.HomeFragment;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class GeofencingOnFragment extends Fragment {
    private TextView geoLocation, geoMsg;
    private EditText geoPass;
    private Button disable;
    private ImageView backBtn;
    private GeofencingInfo geofencingInfo;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private LatLng geofencingLocation;
    private static final String TAG = "GeofencingOnFragment";

    GeofencingOnFragment() {
        // Required empty public constructor
    }
    GeofencingOnFragment(GeofencingInfo geofencingInfo) {
        this.geofencingInfo = geofencingInfo;
        geofencingLocation = new LatLng(Double.parseDouble(geofencingInfo.getLatitude()),Double.parseDouble(geofencingInfo.getLongitude()));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geofencingClient = LocationServices.getGeofencingClient(requireContext());
        geofenceHelper = new GeofenceHelper(requireContext());
        addGeofence();
    }

    @SuppressLint("MissingPermission")
    private void addGeofence(){
        String uid = FirebaseAuth.getInstance().getUid()+"_geofence";
        Geofence geofence = geofenceHelper.getGeofence(uid,geofencingLocation,geofencingInfo.getRadius());
        GeofencingRequest geofencingRequest = geofenceHelper.geofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent(geofencingInfo.getName(),geofencingInfo.getAlertEmail());
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofences added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        Log.d(TAG, "onFailure: "+error);
                    }
                });
        ((MapsActivity)getActivity()).addGeofenceMarker(geofencingLocation,geofencingInfo.getRadius());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_geofencing_on, container, false);
        geoLocation = view.findViewById(R.id.geoLocationTV);
        geoMsg = view.findViewById(R.id.msgGeoTV);
        geoPass = view.findViewById(R.id.geoDisablePasswordET);
        disable = view.findViewById(R.id.disableGeoBtn);
        backBtn = view.findViewById(R.id.backBT5);

        geoLocation.setText(geofencingInfo.getPlaceName());
        geoMsg.setText("Stay within "+geofencingInfo.getRadius()+" metres or\nassigned contact will be alerted");
        handleDisableGeofencing();

        backBtn.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVHome, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        });


        return view;
    }

    boolean geofenceRemoved = false;
    private void handleDisableGeofencing() {
        disable.setOnClickListener(v -> {
            if (geoPass.getText().toString().equals(geofencingInfo.getGeoPin())) {
                geofencingClient.removeGeofences(Collections.singletonList("end_geofence"))
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "onSuccess: Geofences removed...");
                            geofenceRemoved = true;
                        })
                        .addOnFailureListener(e -> {
                            Log.d(TAG, "onFailure: "+e.getMessage());
                        });
                if(!geofenceRemoved)
                    return;
                geofencingInfo.setOn(false);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("geofencing");
                databaseReference.setValue(geofencingInfo);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.FCVHome, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(requireContext(), "Incorrect Passcode", Toast.LENGTH_SHORT).show();
            }
        });
    }
}