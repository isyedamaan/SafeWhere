package com.cyk29.safewhere.mapmodule.geofencing;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.dataclasses.GeofencingInfo;
import com.cyk29.safewhere.helperclasses.GeofenceHelper;
import com.cyk29.safewhere.mapmodule.HomeFragment;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Locale;

public class GeofencingOnFragment extends Fragment {
    private static final String TAG = "GeofencingOnFragment";
    private final GeofencingInfo geofencingInfo;
    private final LatLng geofencingLocation;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private EditText geoPass;
    private Button disable;

    public GeofencingOnFragment(GeofencingInfo geofencingInfo) {
        this.geofencingInfo = geofencingInfo;
        this.geofencingLocation = new LatLng(
                Double.parseDouble(geofencingInfo.getLatitude()),
                Double.parseDouble(geofencingInfo.getLongitude())
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geofencingClient = LocationServices.getGeofencingClient(requireContext());
        geofenceHelper = new GeofenceHelper(requireContext());
        addGeofence();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofencing_on, container, false);
        initializeUI(view);
        return view;
    }

    /**
     * Adds a geofence using the GeofenceHelper.
     */
    @SuppressLint("MissingPermission")
    private void addGeofence() {
        String uid = FirebaseAuth.getInstance().getUid() + "_geofence";
        Geofence geofence = geofenceHelper.getGeofence(uid, geofencingLocation, geofencingInfo.getRadius());
        GeofencingRequest geofencingRequest = geofenceHelper.geofencingRequest
                (geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent(
                geofencingInfo.getName(),
                geofencingInfo.getAlertEmail(),
                geofencingInfo.getAlertNumber()
        );
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: Geofences added..."))
                .addOnFailureListener(e -> {
                    String error = geofenceHelper.getErrorString(e);
                    Log.d(TAG, "onFailure: " + e.getMessage());
                    Log.d(TAG, "onFailure: " + error);
                });
        ((MapsActivity) requireActivity()).addGeofenceMarker(geofencingLocation, geofencingInfo.getRadius());
    }

    /**
     * Initializes the UI components and sets up event listeners.
     */
    private void initializeUI(View view) {
        TextView geoLocation = view.findViewById(R.id.geoLocationTV);
        TextView geoMsg = view.findViewById(R.id.msgGeoTV);
        geoPass = view.findViewById(R.id.geoDisablePasswordET);
        disable = view.findViewById(R.id.disableGeoBtn);
        ImageView backBtn = view.findViewById(R.id.backBT5);
        geoLocation.setText(geofencingInfo.getPlaceName());
        geoMsg.setText(String.format(Locale.getDefault(), "%s%d%s",
                getString(R.string.stay_within),
                geofencingInfo.getRadius(),
                getString(R.string.metres_or_assigned_contact_will_be_alerted))
        );
        handleDisableGeofencing();
        backBtn.setOnClickListener(v -> transitionToHomeFragment());
    }

    /**
     * Sets up the logic for disabling geofencing.
     */
    private void handleDisableGeofencing() {
        disable.setOnClickListener(v -> {
            if (geoPass.getText().toString().equals(geofencingInfo.getGeoPin())) {
                removeGeofence();
            } else {
                ToastHelper.make(requireContext(), "Incorrect Passcode", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * Removes the geofence and updates the state.
     */
    private void removeGeofence() {
        geofencingClient.removeGeofences(Collections.singletonList("end_geofence"))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "onSuccess: Geofences removed...");
                    deactivateGeofencing();
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    /**
     * Deactivates geofencing and transitions to the HomeFragment.
     */
    private void deactivateGeofencing() {
        geofencingInfo.setOn(false);
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            return;
        }
        ((MapsActivity) requireActivity()).removeGeofenceMarker();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("geofencingInfo");
        databaseReference.setValue(geofencingInfo);
        transitionToHomeFragment();
    }

    /**
     * Transitions to the HomeFragment.
     */
    private void transitionToHomeFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, new HomeFragment())
                .addToBackStack(null)
                .commit();
    }
}