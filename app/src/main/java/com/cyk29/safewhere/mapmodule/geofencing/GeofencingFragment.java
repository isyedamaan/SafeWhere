package com.cyk29.safewhere.mapmodule.geofencing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.GeofencingInfo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;

public class GeofencingFragment extends Fragment {
    // UI Components
    private ImageView geoBackButton;
    private TextView title;
    private LinearLayout registerLayout, setupLayout;
    private ConstraintLayout searchLayout, readyLayout;
    private EditText name, alertNumber, alertEmail, geoPin, radius, resetGeofencing;
    private Button mainGeoButton, resetGeoButton;

    private AutocompleteSupportFragment autocompleteFragment;
    private DatabaseReference databaseReference;

    // Variables
    private LatLng latLng;
    private String enteredName,placeName, enteredAlertNumber, enteredAlertEmail, enteredGeoPin, uid;
    private int currentLayout = 0;
    private GeofencingInfo geofencingInfo;


    public GeofencingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_geofencing, container, false);

        initializeUI(view);

        // Firebase UID and Database Reference
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("geofencingInfo");

        // Check if user has already registered for geofencing
        getGeofencingInfoFromDatabase();

        // Initialize Places Client
        if(!Places.isInitialized())
            Places.initialize(requireActivity().getApplicationContext(), getString(R.string.api_key));
        // Google Places and Firebase
//        PlacesClient placesClient = Places.createClient(requireContext());
        // Initialize AutocompleteSupportFragment
        setupAutocompleteFragment();


        // Set onClickListener for Back Button
        geoBackButton.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        // Set onClickListener for Main Geo Button
        mainGeoButton.setOnClickListener(view1 -> {
            switch (currentLayout) {
                case 0:
                    handleRegistration();
                    break;
                case 1:
                    handleSetup();
                    break;
                case 2:
                    geofencingInfo.setOn(true);
                    handleStart();
                    break;
                default:
                    break;
            }
        });

        // Set onClickListener for Reset Geo Button
        handleResetGeofencing();

        return view;
    }

    private void handleResetGeofencing() {
        resetGeoButton.setOnClickListener(view -> {
            String enteredResetGeofencing = resetGeofencing.getText().toString().trim();
            if (enteredResetGeofencing.isEmpty()) {
                showToast("Please fill in all fields");
                return;
            }
            if (enteredResetGeofencing.equals(geofencingInfo.getGeoPin())) {
                databaseReference.removeValue();
                showRegisterLayout();
            } else {
                showToast("Incorrect Geo Pin");
            }
        });
    }

    private void setupAutocompleteFragment() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            // Set up the rest of the fragment
        } else {
            Log.e("AutocompleteError", "AutocompleteSupportFragment is null");
        }

        if (autocompleteFragment != null) {
            autocompleteFragment.requireView().post(() -> {
                ImageView searchIcon = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_button);
                if (searchIcon != null) {
                    searchIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.location_geofence, null));
                    searchIcon.setMaxWidth(40);
                }

                EditText etPlace = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                // Set the hint to empty or to a new placeholder text
                etPlace.setHint("Enter Location");
                etPlace.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.lexend));
                etPlace.setTextSize(18f);
                etPlace.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey));

            });
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onError(@NonNull Status status) {

                }

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    latLng = place.getLatLng();
                    placeName = place.getName();
                }
            });
        }
    }

    private void initializeUI(View view) {
        // Initialize UI elements
        geoBackButton = view.findViewById(R.id.geoBackBT);
        title = view.findViewById(R.id.geoActTitleTV);

        registerLayout = view.findViewById(R.id.register_layout);
        name = view.findViewById(R.id.nameET);
        alertNumber = view.findViewById(R.id.alertNumberET);
        alertEmail = view.findViewById(R.id.alertEmailET);
        geoPin = view.findViewById(R.id.geoPassET);

        setupLayout = view.findViewById(R.id.setup_layout);
        searchLayout = view.findViewById(R.id.searchCL);
        radius = view.findViewById(R.id.radiusET);

        readyLayout = view.findViewById(R.id.ready_layout);
        resetGeofencing = view.findViewById(R.id.resetGeofencingET);
        resetGeoButton = view.findViewById(R.id.resetGeoBT);

        mainGeoButton = view.findViewById(R.id.mainGeoBT);
    }


    private void handleRegistration() {
        enteredName = name.getText().toString().trim();
        enteredAlertNumber = alertNumber.getText().toString().trim();
        enteredAlertEmail = alertEmail.getText().toString().trim();
        enteredGeoPin = geoPin.getText().toString().trim();
        if (enteredName.isEmpty() || enteredAlertNumber.isEmpty() || enteredAlertEmail.isEmpty() || enteredGeoPin.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }
        showSetupLayout();
    }

    private void handleSetup() {
        String enteredRadius = radius.getText().toString().trim();
        if (enteredRadius.isEmpty() || latLng == null) {
            showToast("Please fill in all fields");
            return;
        }
        geofencingInfo = new GeofencingInfo(enteredName, enteredAlertNumber, enteredAlertEmail, enteredGeoPin, placeName,latLng, Integer.parseInt(enteredRadius));
        databaseReference.setValue(geofencingInfo);
        showReadyLayout();
    }

    private void handleStart() {
        showToast("Geofencing started");
        geofencingInfo.setOn(true);
        databaseReference.setValue(geofencingInfo);
        GeofencingOnFragment geofencingOnFragment = new GeofencingOnFragment(geofencingInfo);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, geofencingOnFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showSetupLayout() {
        // Update the visibility of layout elements to show the setup layout
        registerLayout.setVisibility(View.GONE);
        slideAnimation(radius, 200);
        fadeAnimation(searchLayout,0);
        setupLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText(R.string.next);
        title.setText(R.string.select_location);
        currentLayout = 1; // Update the current layout state

    }

    private void showReadyLayout() {
        // Update the visibility of layout elements to show the ready layout
        registerLayout.setVisibility(View.GONE);
        setupLayout.setVisibility(View.GONE);
        readyLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText(R.string.start_geofencing);
        title.setText(R.string.geofencing_ready);
        // You may want to set up additional elements or logic here
        currentLayout = 2; // Update the current layout state
    }

    private void showRegisterLayout() {
        setupLayout.setVisibility(View.GONE);
        readyLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText(R.string.register);
        title.setText(R.string.register);
        currentLayout = 0;

        int delay = 400;
        fadeAnimation(name, delay);
        slideAnimation(alertNumber, delay + 200);
        slideAnimation(alertEmail, delay + 400);
        slideAnimation(geoPin, delay + 600);
    }

    public void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void slideAnimation(View view, int delay) {
        Animation slideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
        slideDown.setStartOffset(delay);
        view.startAnimation(slideDown);
    }

    public void fadeAnimation(View view, int delay) {
        Animation fade = AnimationUtils.loadAnimation(requireContext(), R.anim.fade);
        fade.setStartOffset(delay);
        view.startAnimation(fade);
    }

    private void getGeofencingInfoFromDatabase() {
        DatabaseReference geoInfoRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("geofencingInfo");
        geoInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    geofencingInfo = dataSnapshot.getValue(GeofencingInfo.class);
                    currentLayout = 2;
                    showReadyLayout();
                    showToast("Geofencing already registered");
                    if(geofencingInfo.isOn())
                        handleStart();
                } else {
                    showRegisterLayout();
                    showToast("Geofencing not registered");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
    }
}