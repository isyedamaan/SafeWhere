package com.cyk29.safewhere.mapmodule;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cyk29.safewhere.R;
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

public class OnRouteFragment extends Fragment {
    private static final String TAG = "OnRouteFragment";
    private LatLng destinationLatLng;
    private String placeName;
    public OnRouteFragment() {
    }
    public OnRouteFragment(LatLng destinationLatLng, String placeName) {
        this.destinationLatLng = destinationLatLng;
        this.placeName = placeName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_route, container, false);
        initializeUI(view);

        LatLng origin = ((MapsActivity) requireActivity()).getUserLatLng();
        Log.d("DestinationSelect", "onCreateView: 2 "+origin);
        fetchDirections(origin, destinationLatLng);
        return view;
    }
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void fetchDirections(LatLng origin, LatLng destination) {
        executorService.submit(() -> {
            List<LatLng> decodedPath = getDirections(origin, destination);
            if(decodedPath == null) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), "Error fetching directions", Toast.LENGTH_SHORT).show());
                return;
            }
            (requireActivity()).runOnUiThread(() -> ((MapsActivity) requireActivity()).makePolyLineForRoute(decodedPath));
        });
    }

    private void initializeUI(View view) {
        TextView destinationTV = view.findViewById(R.id.on_route_destinationTV);
        destinationTV.setText(placeName);
        Button endBtn = view.findViewById(R.id.endBtn);
        endBtn.setOnClickListener(v -> {
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        });
    }

    private List<LatLng> getDirections(LatLng origin, LatLng destination) {
        // Create a Uri object with the base Google Directions URL and your parameters
        Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("origin", origin.latitude + "," + origin.longitude)
                .appendQueryParameter("destination", destination.latitude + "," + destination.longitude)
                .appendQueryParameter("key", "AIzaSyDPquUgkBjQ4fuz7dh0zY9nQ_szXhv35ks")
                .build();
        Log.d(TAG, "getDirections:  "+uri.toString());
        // Make the HTTP request
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

    private List<LatLng> convertJsonToLatLngList(String routeJson) {
        List<LatLng> latLngList;
        Log.d(TAG, "convertJsonToLatLngList:     "+routeJson);
        try {
            // Convert the JSON string to a JSONObject
            JSONObject jsonObject = new JSONObject(routeJson);

            // Depending on the structure of your JSON, the path to the coordinates array may vary
            // This is a generic example, adjust the keys as per your JSON structure
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
        Log.d(TAG, "convertJsonToLatLngList:   size:  " + latLngList.size() + " ");
        return latLngList;
    }

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