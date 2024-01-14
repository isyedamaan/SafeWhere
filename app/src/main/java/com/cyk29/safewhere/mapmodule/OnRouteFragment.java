package com.cyk29.safewhere.mapmodule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.reportmodule.ReportMainActivity;
import com.cyk29.safewhere.sosmodule.SosActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fragment for handling the 'On Route' functionality in the application.
 * This fragment handles map directions and user interaction while on a route.
 */
public class OnRouteFragment extends Fragment {
    private static final String TAG = "OnRouteFragment";
    private LatLng destinationLatLng;
    private String placeName;

    /**
     * Default constructor for the fragment.
     */
    public OnRouteFragment() {
    }

    /**
     * Constructor with parameters for destination latitude/longitude and place name.
     * @param destinationLatLng Latitude and longitude of the destination.
     * @param placeName Name of the destination place.
     */
    public OnRouteFragment(LatLng destinationLatLng, String placeName) {
        this.destinationLatLng = destinationLatLng;
        this.placeName = placeName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_route, container, false);
        initializeUI(view);
        setupRoute();
        return view;
    }

    /**
     * Initializes the UI elements of the fragment.
     *
     * @param view The view inflated for the fragment.
     */
    private void initializeUI(View view) {
        TextView destinationTV = view.findViewById(R.id.on_route_destinationTV);
        destinationTV.setText(placeName);
        Button endBtn = view.findViewById(R.id.endBtn);
        endBtn.setOnClickListener(v -> {
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        });
        ImageView report = view.findViewById(R.id.onRouteReportBtn);
        report.setOnClickListener(v -> startActivity(new Intent(requireActivity(), ReportMainActivity.class)));
        ImageView sos = view.findViewById(R.id.onRouteSOSBtn);
        sos.setOnClickListener(v -> startActivity(new Intent(requireActivity(), SosActivity.class)));
    }

    /**
     * Sets up the route by fetching directions and updating the UI accordingly.
     */
    private void setupRoute() {
        ((MapsActivity) requireActivity()).backBtn.setVisibility(View.GONE);
        LatLng origin = ((MapsActivity) requireActivity()).getUserLatLng();
        executeGetDirectionsTask(origin, destinationLatLng);
    }

    /**
     * Starts the getDirections task asynchronously  to get directions from the origin
     * to the destination using the Google Directions API.
     * Executor service is used to run the task on a separate thread.
     * @param origin      Starting point of the route.
     * @param destination Destination point of the route.
     */
    private void executeGetDirectionsTask(LatLng origin, LatLng destination) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            List<LatLng> decodedPath = getDirections(origin, destination);
            if(decodedPath == null) {
                requireActivity().runOnUiThread(() -> ToastHelper.make(requireContext(),"Error Fetching Directions", Toast.LENGTH_LONG));
                return;
            }
            (requireActivity()).runOnUiThread(() -> ((MapsActivity) requireActivity()).setPolylineOptionsForRoute(decodedPath));
        });
    }



    /**
     * Fetches the directions from Google Maps Directions API.
     * This method builds a URL with the origin, destination, and API key, and makes an HTTP request to the API.
     * Which returns a decoded string of directions.
     * If everything is error-free, the string is decoded into a list of LatLng points.
     * @param origin      The starting point of the route (latitude and longitude).
     * @param destination The ending point of the route (latitude and longitude).
     * @return A list of LatLng points representing the path of the route.
     *         Returns null if there is an error in fetching or parsing the data.
     */
    private List<LatLng> getDirections(LatLng origin, LatLng destination) {
        Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("origin", origin.latitude + "," + origin.longitude)
                .appendQueryParameter("destination", destination.latitude + "," + destination.longitude)
                .appendQueryParameter("key", "AIzaSyDPquUgkBjQ4fuz7dh0zY9nQ_szXhv35ks")
                .build();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            if (stream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            if (result.length() > 0) {
                return convertJsonToLatLngList(result.toString());
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Converts the JSON response from the Google Maps Directions API into a list of LatLng points.
     * This method parses the JSON string, extracts the polyline points, and decodes them into LatLng points.
     *
     * @param routeJson The JSON string returned by the Google Maps Directions API.
     * @return A list of LatLng points representing the path of the route.
     *         Returns null if there is an error in parsing the JSON string.
     */
    private List<LatLng> convertJsonToLatLngList(String routeJson) {
        List<LatLng> latLngList;
        try {
            JSONObject jsonObject = new JSONObject(routeJson);
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            JSONObject route = routesArray.getJSONObject(0);
            JSONObject poly = route.getJSONObject("overview_polyline");
            String polyline = poly.getString("points");
            // Decode the polyline string to a list of LatLng
            latLngList = decodePolyline(polyline);
        } catch (Exception e) {
            Log.d(TAG, "convertJsonToLatLngList: " + e.getMessage());
            return null;
        }
        return latLngList;
    }

    /**
     * Decodes an encoded polyline string into a list of LatLng points.
     * The encoding algorithm is a compact form of representing a list of coordinates as a single string.
     *
     * @param encoded The encoded polyline string.
     * @return A list of LatLng objects representing the decoded polyline points.
     */
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> list = new ArrayList<>();

        int len = encoded.length();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(((double) lat / 1E5),
                    ((double) lng / 1E5));
            list.add(p);
        }
        return list;
    }

}