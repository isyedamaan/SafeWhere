package com.cyk29.safewhere.mapmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataObjects.GeofencingInfo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;
import java.util.Objects;

public class GeofencingActivity extends AppCompatActivity {
    // UI Components
    private ImageView geoBackButton;
    private TextView title;
    private LinearLayout registerLayout, setupLayout;
    private ConstraintLayout searchLayout, readyLayout;
    private EditText name, alertNumber, alertEmail, geoPin, radius, resetGeofencing;
    private Button mainGeoButton, resetGeoButton;

    // Google Places and Firebase
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    private DatabaseReference databaseReference;

    // Variables
    private LatLng latLng;
    private String enteredName, enteredAlertNumber, enteredAlertEmail, enteredGeoPin, uid;
    private int currentLayout = 0;
    private boolean alreadyRegistered = false;
    private GeofencingInfo geofencingInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);

        initializeUI();

        // Firebase UID and Database Reference
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("geofencingInfo");

        // Check if user has already registered for geofencing
        getGeofencingInfoFromDatabase();

        // Initialize Places Client
        if(!Places.isInitialized())
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(this);

        // Initialize AutocompleteSupportFragment
        setupAutocompleteFragment();


        // Set onClickListener for Back Button
        geoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Set onClickListener for Main Geo Button
        mainGeoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentLayout) {
                    case 0:
                        handleRegistration();
                        break;
                    case 1:
                        handleSetup();
                        break;
                    case 2:
                        handleStart();
                        break;
                    default:
                        break;
                }
            }
        });

        // Set onClickListener for Reset Geo Button
        handleResetGeofencing();

    }

    private void handleResetGeofencing() {
        resetGeoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    private void setupAutocompleteFragment() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            // Set up the rest of the fragment
        } else {
            Log.e("AutocompleteError", "AutocompleteSupportFragment is null");
        }

        if (autocompleteFragment != null) {
            autocompleteFragment.getView().post(new Runnable() {
                @Override
                public void run() {
                    ImageView searchIcon = (ImageView) autocompleteFragment.getView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_button);
                    if (searchIcon != null) {
                        searchIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.location_geofence, null));
                        searchIcon.setMaxWidth(40);
                    }

                    EditText etPlace = (EditText) autocompleteFragment.getView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                    // Set the hint to empty or to a new placeholder text
                    etPlace.setHint("Enter Location");
                    etPlace.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.lexend));
                    etPlace.setTextSize(18f);
                    etPlace.setTextColor(getResources().getColor(R.color.grey));

                }
            });
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onError(@NonNull Status status) {

                }

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    latLng = place.getLatLng();
                }
            });
        }
    }

    private void initializeUI() {
        // Initialize UI elements
        geoBackButton = findViewById(R.id.geoBackBT);
        title = findViewById(R.id.geoActTitleTV);

        registerLayout = findViewById(R.id.register_layout);
        name = findViewById(R.id.nameET);
        alertNumber = findViewById(R.id.alertNumberET);
        alertEmail = findViewById(R.id.alertEmailET);
        geoPin = findViewById(R.id.geoPassET);

        setupLayout = findViewById(R.id.setup_layout);
        searchLayout = findViewById(R.id.searchCL);
        radius = findViewById(R.id.radiusET);

        readyLayout = findViewById(R.id.ready_layout);
        resetGeofencing = findViewById(R.id.resetGeofencingET);
        resetGeoButton = findViewById(R.id.resetGeoBT);

        mainGeoButton = findViewById(R.id.mainGeoBT);
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
        geofencingInfo = new GeofencingInfo(enteredName, enteredAlertNumber, enteredAlertEmail, enteredGeoPin, latLng, Integer.parseInt(enteredRadius));
        databaseReference.setValue(geofencingInfo);
        showReadyLayout();
    }

    private void handleStart() {

    }

    private void showSetupLayout() {
        // Update the visibility of layout elements to show the setup layout
        registerLayout.setVisibility(View.GONE);
        slideAnimation(radius, 200);
        fadeAnimation(searchLayout,0);
        setupLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText("Next");
        title.setText("Select Location");
        currentLayout = 1; // Update the current layout state

    }

    private void showReadyLayout() {
        // Update the visibility of layout elements to show the ready layout
        registerLayout.setVisibility(View.GONE);
        setupLayout.setVisibility(View.GONE);
        readyLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText("Start Geofencing");
        title.setText("Geofencing Ready");
        // You may want to set up additional elements or logic here
        currentLayout = 2; // Update the current layout state
    }

    private void showRegisterLayout() {
        setupLayout.setVisibility(View.GONE);
        readyLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText("Register");
        title.setText("Register");
        currentLayout = 0;

        int delay = 400;
        fadeAnimation(name, delay);
        slideAnimation(alertNumber, delay + 200);
        slideAnimation(alertEmail, delay + 400);
        slideAnimation(geoPin, delay + 600);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void slideAnimation(View view, int delay) {
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slideDown.setStartOffset(delay);
        view.startAnimation(slideDown);
    }

    public void fadeAnimation(View view, int delay) {
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        fade.setStartOffset(delay);
        view.startAnimation(fade);
    }

    private boolean getGeofencingInfoFromDatabase() {
        DatabaseReference geoInfoRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("geofencingInfo");
        geoInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    geofencingInfo = dataSnapshot.getValue(GeofencingInfo.class);
                    alreadyRegistered = true;
                    currentLayout = 2;
                    showReadyLayout();
                    showToast("Geofencing already registered");
                } else {
                    alreadyRegistered = false;
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
        return alreadyRegistered;
    }
}
