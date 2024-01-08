package com.cyk29.safewhere.mapmodule;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.Report;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HotSpotHelper extends ContextWrapper implements HotSpotFragment.BottomSheetListener{
    private static final String TAG = "HotSpotHelper";
    public HotSpotHelper(Context base) {
        super(base);
    }
    private final int radius = 25000;
    public void addMarkersForNearbyReports(GoogleMap map, Location userLocation) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    Log.d(TAG, "onDataChange: "+isWithinRadius(userLocation, report, 5000));
                    if (isWithinRadius(userLocation, report, radius)) {
                        LatLng reportLocation = new LatLng(Double.parseDouble(report.getLatitude()), Double.parseDouble(report.getLongitude()));
                        makeReportMarker(map, reportLocation, report);
                        Log.d(TAG, "onDataChange: got a report brudda");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        map.setOnMarkerClickListener(marker -> {
            Report report = (Report) marker.getTag();
            if (report != null) {
                showReportDetails(report);

            }
            return false;
        });
    }

    List<Marker> markers = new ArrayList<>();
    List<Circle> circles = new ArrayList<>();
    private void makeReportMarker(GoogleMap map, LatLng reportLocation, Report report) {
        MarkerOptions markerOptions = new MarkerOptions().position(reportLocation)
                .title("Danger Zone")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ic_hotspot));
        Marker marker =  map.addMarker(markerOptions);
        marker.setTag(report);
        markers.add(marker);
        Circle circle1 = map.addCircle(new CircleOptions()
                .center(reportLocation)
                .radius(80)
                .strokeWidth(0)
                .fillColor(Color.argb(50, 255, 0, 0)));
        Circle circle2 = map.addCircle(new CircleOptions()
                .center(reportLocation)
                .radius(160)
                .strokeWidth(0)
                .fillColor(Color.argb(50, 255, 0, 0)));
        Circle circle3 = map.addCircle(new CircleOptions()
                .center(reportLocation)
                .radius(240)
                .strokeWidth(0)
                .fillColor(Color.argb(50, 255, 0, 0)));
        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);
    }

    private boolean isWithinRadius(Location userLocation, Report report, int radius) {
        float[] results = new float[1];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                Double.parseDouble(report.getLatitude()), Double.parseDouble(report.getLongitude()),
                results);
        return results[0] <= radius;
    }

    private void showReportDetails(Report report) {
        HotSpotFragment hotSpotFragment = new HotSpotFragment(report);
        hotSpotFragment.setBottomSheetListener(this);
        hotSpotFragment.show(((MapsActivity) getBaseContext()).getSupportFragmentManager(), "HotSpotFragment");
    }

    @Override
    public void showHotspots() {
        clearMarkers();
        ((MapsActivity) getBaseContext()).showHotSpots();
    }

    public void clearMarkers() {
        Iterator<Marker> markerIterator = markers.iterator();
        while (markerIterator.hasNext()) {
            Marker marker = markerIterator.next();
            marker.remove();
            markerIterator.remove();
        }
        Iterator<Circle> circleIterator = circles.iterator();
        while (circleIterator.hasNext()) {
            Circle circle = circleIterator.next();
            circle.remove();
            circleIterator.remove();
        }
    }
}
