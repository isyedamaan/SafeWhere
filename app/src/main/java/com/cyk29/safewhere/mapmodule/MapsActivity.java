package com.cyk29.safewhere.mapmodule;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.notificationmodule.NotificationMainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cyk29.safewhere.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(3.12049,101.6510061);
    private LatLng currentLocation;


    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        if (locationPermissionGranted) {
            // Try to obtain the last known location
            getDeviceLocation();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();
        updateLocationUI();

        if (locationPermissionGranted) {
            // Try to obtain the last known location
            getDeviceLocation();
        }
        if(currentLocation == null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15.0f));
        }
        else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
        }
        if(currentLocation!= null){
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(currentLocation)
                    .title("Marker Title")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
            mMap.addMarker(markerOptions);
        }


    }
    private void getDeviceLocation() {
//        try {
//            if (locationPermissionGranted) {
//                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Set the map's camera position to the current location of the device.
//                        Location lastKnownLocation = task.getResult();
//                        if (lastKnownLocation != null) {
//                            currentLocation = new LatLng(lastKnownLocation.getLatitude(),
//                                    lastKnownLocation.getLongitude());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
//                        }
//                    } else {
//                        Log.d("MapsActivity", "Current location is null. Using defaults.");
//                        Log.e("MapsActivity", "Exception: %s", task.getException());
//                        mMap.moveCamera(CameraUpdateFactory
//                                .newLatLngZoom(defaultLocation, 15.0f));
//                    }
//                });
//            }
//        } catch (SecurityException e) {
//            Log.e("Exception: %s", e.getMessage(), e);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
//        if (mMap == null) {
//            return;
//        }
//        try {
//            if (locationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                getLocationPermission(); // Request permissions again if not granted
//            }
//        } catch (SecurityException e) {
//            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
//        }
    }

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (!locationPermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            updateLocationUI();
        }
    }


    void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}