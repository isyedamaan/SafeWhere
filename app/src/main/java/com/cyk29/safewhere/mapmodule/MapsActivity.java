package com.cyk29.safewhere.mapmodule;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.cyk29.safewhere.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cyk29.safewhere.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 15.0f;


        // Add a marker in Sydney and move the camera
        LatLng kl = new LatLng(3.120490, 101.653581);
        Marker tempMarker = mMap.addMarker(new MarkerOptions().position(kl).title("UM Central"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kl,zoomLevel));

        mMap.setOnMarkerClickListener(marker -> {
            if (marker.equals(tempMarker)) {


                return true; // Consume the event to prevent default behavior (info window)
            }
            return false; // Let the default behavior happen for other markers
        });



    }
}