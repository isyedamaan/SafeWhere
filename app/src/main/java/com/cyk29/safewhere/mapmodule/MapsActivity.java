package com.cyk29.safewhere.mapmodule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private static final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 10003;
    private static final int SMS_PERMISSION_REQUEST_CODE = 10004;
    private static final int CALL_PERMISSION_REQUEST_CODE = 10005;
    public GoogleMap mMap;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.cyk29.safewhere.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        getLastLocation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        enableUserLocation();
        handleNotificationPermission();
        handleSMSandCallPermissions();
        showHotSpots();

        mMap.setOnMapLongClickListener(latLng -> addGeofenceMarker(latLng, 1000));
    }

    public void showHotSpots() {

        HotSpotHelper hotSpotHelper = new HotSpotHelper(this);
        if(currentLocation != null){
            hotSpotHelper.addMarkersForNearbyReports(mMap, currentLocation);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 13));
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .radius(2500)
                    .strokeWidth(2)
                    .strokeColor(ContextCompat.getColor(this, R.color.grey)));
        } else {
            Location location = new Location("");
            location.setLatitude(3.1224671);
            location.setLongitude(101.6499131);
            hotSpotHelper.addMarkersForNearbyReports(mMap, location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(3.1224671, 101.6499131), 13));
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(3.1224671, 101.6499131))
                    .radius(2500)
                    .strokeWidth(2)
                    .strokeColor(ContextCompat.getColor(this, R.color.grey)));
        }
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            enableUserLocation();
        }
        if(Build.VERSION.SDK_INT >= 29){
            // need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                enableUserLocation();
            }
        }
    }

    private void handleNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void handleSMSandCallPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.clear();
                showHotSpots();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Fine Location Permission");
                builder.setMessage("You need to grant location permission to use this app. Grant permission in app settings.");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(android.net.Uri.parse("package:"+getPackageName()));
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> finish());
            }
        }
        if(requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: background location permission granted");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Background Location Permission");
                builder.setMessage("You need to grant background location permission to use this app. Grant permission in app settings.");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(android.net.Uri.parse("package:"+getPackageName()));
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> finish());
            }
        }
        if(requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: notification permission granted");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notification Permission");
                builder.setMessage("You need to grant notification permission to use this app. Grant permission in app settings.");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(android.net.Uri.parse("package:"+getPackageName()));
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> finish());
            }
        }
        if(requestCode == SMS_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: sms permission granted");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("SMS Permission");
                builder.setMessage("You need to grant SMS permission to properly use this app. Grant permission in app settings.");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(android.net.Uri.parse("package:"+getPackageName()));
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> finish());
            }
        }
        if(requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: call permission granted");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Call Permission");
                builder.setMessage("You need to grant call permission to properly use this app. Grant permission in app settings.");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> finish());
            }
        }
    }

    public void addGeofenceMarker(LatLng latLng, int radius){
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Geofencing Area")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.geofence_marker)));
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeWidth(3f)
                .strokeColor(ContextCompat.getColor(this,R.color.blue))
                .fillColor(ContextCompat.getColor(this,R.color.blue_transparent)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> currentLocation = location);
    }
    void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}