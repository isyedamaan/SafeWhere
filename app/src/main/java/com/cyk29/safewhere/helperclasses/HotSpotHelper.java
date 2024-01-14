package com.cyk29.safewhere.helperclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.Report;
import com.cyk29.safewhere.mapmodule.HotSpotFragment;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for managing hotspot markers and interactions on the map.
 */
public class HotSpotHelper extends ContextWrapper implements HotSpotFragment.BottomSheetListener {
    private static final String TAG = "HotSpotHelper";

    /**
     * Constructs a new HotSpotHelper.
     *
     * @param base The base context.
     */
    public HotSpotHelper(Context base) {
        super(base);
    }

    /**
     * Add markers for nearby reports on the map.
     *
     * @param map         The GoogleMap instance.
     * @param userLocation The user's current location.
     */
    @SuppressLint("PotentialBehaviorOverride")
    public void addMarkersForNearbyReports(GoogleMap map, Location userLocation) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    if (report == null) {
                        continue;
                    }
                    if (isWithinRadius(userLocation, report)) {
                        LatLng reportLocation = new LatLng(Double.parseDouble(report.getLatitude()), Double.parseDouble(report.getLongitude()));
                        makeReportMarker(map, reportLocation, report);
                        Log.d(TAG, "onDataChange: got a report");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+databaseError.toException().getLocalizedMessage());
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




    private final List<Marker> markers = new ArrayList<>();
    private final List<Circle> circles = new ArrayList<>();

    /**
     * Create and add a report marker on the map.
     *
     * @param map           The GoogleMap instance.
     * @param reportLocation The LatLng location of the report.
     * @param report        The report data.
     */
    private void makeReportMarker(GoogleMap map, LatLng reportLocation, Report report) {
        MarkerOptions markerOptions = new MarkerOptions().position(reportLocation)
                .title("Danger Zone")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ic_hotspot));
        Marker marker = map.addMarker(markerOptions);
        if (marker != null) {
            marker.setTag(report);
        }
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

    /**
     * Check if a report is within a specified radius from the user's location.
     *
     * @param userLocation The user's current location.
     * @param report       The report data.
     * @return True if the report is within the radius, false otherwise.
     */
    private boolean isWithinRadius(Location userLocation, Report report) {
        float[] results = new float[1];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                Double.parseDouble(report.getLatitude()), Double.parseDouble(report.getLongitude()),
                results);
        return results[0] <= 2500;
    }

    /**
     * Show details of a report in a bottom sheet dialog.
     *
     * @param report The report data to display.
     */
    private void showReportDetails(Report report) {
        HotSpotFragment hotSpotFragment = new HotSpotFragment(report);
        hotSpotFragment.setBottomSheetListener(this);
        hotSpotFragment.show(((MapsActivity) getBaseContext()).getSupportFragmentManager(), "HotSpotFragment");
    }

    @Override
    public void showHotspots() {
        clearMarkers();
        ((MapsActivity) getBaseContext()).mMap.clear();
        ((MapsActivity) getBaseContext()).showHotSpots();
    }

    /**
     * Clear all markers and circles from the map.
     */
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

    /**
     * Add markers near a polyline on the map.
     *
     * @param map      The GoogleMap instance.
     * @param polyline The polyline.
     */
    public void addMarkersNearPolyline(GoogleMap map, Polyline polyline) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    if (report == null) {
                        continue;
                    }

                    LatLng reportLocation = new LatLng(Double.parseDouble(report.getLatitude()), Double.parseDouble(report.getLongitude()));
                    if (isPerpendicularlyNearPolyline(polyline, reportLocation)) {
                        makeReportMarker(map, reportLocation, report);
                        Log.d(TAG, "onDataChange: got a report near polyline");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    /**
     * Check if a location is perpendicularly near a polyline.
     *
     * @param polyline       The polyline.
     * @param reportLocation The location to check.
     * @return True if the location is perpendicularly near the polyline, false otherwise.
     */
    private boolean isPerpendicularlyNearPolyline(Polyline polyline, LatLng reportLocation) {
        List<LatLng> points = polyline.getPoints();
        for (int i = 0; i < points.size() - 1; i++) {
            if (distanceToLineSegment(points.get(i), points.get(i + 1), reportLocation) <= 250) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate the distance from a point to a line segment.
     *
     * @param start  The starting point of the line segment.
     * @param end    The ending point of the line segment.
     * @param point  The point to calculate distance from.
     * @return The distance from the point to the line segment.
     */
    private double distanceToLineSegment(LatLng start, LatLng end, LatLng point) {
        return SphericalUtil.computeDistanceBetween(closestPoint(start, end, point), point);
    }

    /**
     * Calculate the closest point on a line segment to a given point.
     *
     * @param start The starting point of the line segment.
     * @param end   The ending point of the line segment.
     * @param point The point to find the closest point to.
     * @return The closest point on the line segment to the given point.
     */
    private LatLng closestPoint(LatLng start, LatLng end, LatLng point) {
        double l2 = SphericalUtil.computeDistanceBetween(start, end) * SphericalUtil.computeDistanceBetween(start, end);
        if (l2 == 0) return start;
        double t = ((point.latitude - start.latitude) * (end.latitude - start.latitude) + (point.longitude - start.longitude) * (end.longitude - start.longitude)) / l2;
        t = Math.max(0, Math.min(1, t));
        return new LatLng(start.latitude + t * (end.latitude - start.latitude), start.longitude + t * (end.longitude - start.longitude));
    }
}
