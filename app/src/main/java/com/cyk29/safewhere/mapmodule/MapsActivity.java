package com.cyk29.safewhere.mapmodule;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.databinding.ActivityMapsBinding;
import com.cyk29.safewhere.helperclasses.HotSpotHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * MapsActivity is responsible for displaying and managing a Google Map.
 * It handles user location, geofencing, and displays hotspots using Firebase.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    public GoogleMap mMap;
    private Location currentLocation;
    ImageView backBtn;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Marker geofenceMarker;
    private Circle geofenceCircle;
    private HotSpotHelper hotSpotHelper;
    private PolylineOptions polylineOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeMap();
        setupBackButton();
        getLastLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    /**
     * Called when the map is ready for use.
     * @param googleMap The GoogleMap object.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        configureMapStyle();
        enableUserLocation();
        showHotSpots();
        sendHotspotNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Initializes the map fragment.
     */
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Sets up the back button for navigation.
     */
    private void setupBackButton() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    /**
     * Retrieves the updates for last known location of the user.
     */
    private void getLastLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> currentLocation = location);
        createLocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    currentLocation = locationResult.getLastLocation();
                    Log.d(TAG, "onLocationResult: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                    if(polylineOptions == null){
                        mMap.clear();
                        showHotSpots();
                        if(geofenceCircle != null){
                            mMap.addCircle(new CircleOptions()
                                    .center(geofenceCircle.getCenter())
                                    .radius(geofenceCircle.getRadius())
                                    .fillColor(geofenceCircle.getFillColor())
                                    .strokeWidth(geofenceCircle.getStrokeWidth())
                                    .strokeColor(geofenceCircle.getStrokeColor()));
                            mMap.addMarker(new MarkerOptions().position(geofenceMarker.getPosition()).title("Geofencing Area")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.geofence_marker)));
                        }
                    }
                }
            }
        };
    }

    /**
     * Configures the style of the map.
     */
    private void configureMapStyle() {
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
    }

    /**
     * Enables user location on the map if permission is granted.
     */
    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setPadding(0, 0, 0, 160);
    }

    /**
     * Displays hotspots on the map based on the current location.
     */
    public void showHotSpots() {
        hotSpotHelper = new HotSpotHelper(this);
        LatLng latLng;
        if (currentLocation != null) {
            hotSpotHelper.addMarkersForNearbyReports(mMap, currentLocation);
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            Location location = new Location("");
            location.setLatitude(3.1224671);
            location.setLongitude(101.6499131);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(2500)
                .strokeWidth(2)
                .strokeColor(ContextCompat.getColor(this, R.color.grey)));
    }

    /**
     * Sends a notification about hotspots in the area.
     */
    private void sendHotspotNotification() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> ToastHelper.make(this, "Hotspots around your area are being displayed now.", Toast.LENGTH_LONG), 1000);
        String uid = FirebaseAuth.getInstance().getUid();
        if(uid == null){
            Log.d(TAG, "sendHotspotNotification: uid is null");
            return;
        }
        DatabaseReference ref = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("users").child(uid).child("notifications");
        String id = ref.push().getKey();
        if(id == null){
            Log.d(TAG, "sendHotspotNotification: id is null");
            return;
        }
        ref.child(id).setValue(new com.cyk29.safewhere.dataclasses.NotificationItem(id, "Hotspots around your area are being displayed now.", String.valueOf(System.currentTimeMillis()), "Hotspot"));
    }

    /**
     * Starts requesting location updates.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    /**
     * Stops requesting location updates.
     */
    private void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    /**
     * Creates a request for location updates.
     */
    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000
        ).setMinUpdateDistanceMeters(1000)
                .setMinUpdateIntervalMillis(5000)
                .build();
    }

    /**
     * Adds a geofence marker on the map.
     * @param latLng The LatLng for the center of the geofence.
     * @param radius The radius of the geofence in meters.
     */
    public void addGeofenceMarker(LatLng latLng, int radius) {
        geofenceMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Geofencing Area")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.geofence_marker)));
        geofenceCircle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeWidth(3f)
                .strokeColor(ContextCompat.getColor(this, R.color.blue))
                .fillColor(ContextCompat.getColor(this, R.color.blue_transparent)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    /**
     * Removes the geofence marker from the map.
     */
    public void removeGeofenceMarker() {
        if(geofenceMarker != null)
            geofenceMarker.remove();
        if(geofenceCircle != null)
            geofenceCircle.remove();
    }

    /**
     * Sets polyline options for a route retrieved from directions API and displays it on the map.
     * @param decodedPath The list of LatLng points that form the polyline.
     */
    public void setPolylineOptionsForRoute(List<LatLng> decodedPath) {
        polylineOptions = new com.google.android.gms.maps.model.PolylineOptions().addAll(decodedPath).width(10).color(ContextCompat.getColor(this, R.color.blue));
        mMap.clear();
        Polyline polyline = mMap.addPolyline(polylineOptions);
        mMap.addMarker(new MarkerOptions().position(decodedPath.get(0)).title("Start")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
        mMap.addMarker(new MarkerOptions().position(decodedPath.get(decodedPath.size()-1)).title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destionation_marker)));
        hotSpotHelper.addMarkersNearPolyline(mMap, polyline);
    }

    /**
     * Gets the user's current LatLng.
     * @return The current LatLng of the user.
     */
    public LatLng getUserLatLng() {
        if (currentLocation == null) {
            getLastLocationUpdates();
            return getUserLatLng();
        }
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }
}
