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
import com.cyk29.safewhere.helperclasses.ToastHelper;
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

/**
 * A Fragment class for managing geofencing in the SafeWhere application.
 * This class handles the user interface and logic for setting up, starting,
 * and resetting geofencing based on user inputs. It interacts with Firebase
 * to store and retrieve geofencing information.
 */
public class GeofencingFragment extends Fragment {
    private static final String TAG = "GeofencingFragment";

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
    private String enteredName;
    private String placeName;
    private String enteredAlertNumber;
    private String enteredAlertEmail;
    private String enteredGeoPin;
    private int currentLayout = 0;
    private GeofencingInfo geofencingInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofencing, container, false);
        initializeUI(view);
        initializeFirebaseAndPlaces();
        setupAutocompleteFragment();
        setupButtonListeners();
        return view;
    }

    /**
     * Initializes UI components from the XML layout.
     * @param view The root view of the fragment's layout.
     */
    private void initializeUI(View view) {
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

    /**
     * Initializes Firebase authentication and database references, and sets up the Google Places API.
     * This method sets the user ID for Firebase operations and checks if the Places API is initialized.
     * Also checks if the user has existing geofencing information in the database.
     * If not, it initializes the Places API with the required API key.
     */
    private void initializeFirebaseAndPlaces() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("geofencingInfo");
        checkExistingGeofencingInfo();
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), getString(R.string.api_key));
        }
    }


    /**
     * Sets up the AutocompleteSupportFragment for location searching.
     * This method customizes the appearance and behavior of the autocomplete fragment.
     * It also handles the selection and error events when a place is chosen or an error occurs.
     */
    private void setupAutocompleteFragment() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            autocompleteFragment.requireView().post(() -> {
                ImageView searchIcon = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_button);
                if (searchIcon != null) {
                    searchIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.location_geofence, null));
                    searchIcon.setMaxWidth(40);
                }
                EditText etPlace = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                if (etPlace != null) {
                    etPlace.setHint("Enter Location");
                    etPlace.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.lexend));
                    etPlace.setTextSize(18f);
                    etPlace.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                }
            });
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onError(@NonNull Status status) {
                    Log.e(TAG, "An error occurred: " + status.getStatusMessage());
                }

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    latLng = place.getLatLng();
                    placeName = place.getName();
                }
            });
        } else {
            Log.e(TAG, "setupAutocompleteFragment: AutocompleteSupportFragment is null");
        }
    }


    /**
     * Sets up listeners for various buttons in the fragment.
     */
    private void setupButtonListeners() {
        geoBackButton.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mainGeoButton.setOnClickListener(view -> handleMainGeoButton());
        resetGeoButton.setOnClickListener(view -> handleResetGeofencing());
    }

    /**
     * Handles actions to be performed when the main geofencing button is clicked.
     * Depending on the current layout state (registration, setup, or start), it performs the respective action.
     */
    private void handleMainGeoButton() {
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
                Log.e(TAG, "Invalid layout state: " + currentLayout);
                break;
        }
    }

    /**
     * Handles the registration process for geofencing.
     * Validates and stores user input data and transitions the UI to the setup layout if validation passes.
     */

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

    /**
     * Handles the setup process for geofencing.
     * Validates the radius input and stores the geofencing information in the database.
     * Transitions the UI to the ready layout if validation passes.
     */
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

    /**
     * Handles the starting of geofencing.
     * Sets the geofencing to be active and stores this information in the database.
     * Transitions to the GeofencingOnFragment to manage the active geofencing.
     */
    private void handleStart() {
        geofencingInfo.setOn(true);
        databaseReference.setValue(geofencingInfo);
        GeofencingOnFragment geofencingOnFragment = new GeofencingOnFragment(geofencingInfo);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, geofencingOnFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Updates the UI to show the setup layout.
     * Animates the transition and updates the main button text and title.
     */
    private void showSetupLayout() {
        registerLayout.setVisibility(View.GONE);
        slideAnimation(radius, 200);
        fadeAnimation(searchLayout,0);
        setupLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText(R.string.next);
        title.setText(R.string.select_location);
        currentLayout = 1;
    }

    /**
     * Updates the UI to show the ready layout, indicating that geofencing is ready to be started.
     * Hides the registration and setup layouts, and updates the main button text and title.
     */
    private void showReadyLayout() {
        registerLayout.setVisibility(View.GONE);
        setupLayout.setVisibility(View.GONE);
        readyLayout.setVisibility(View.VISIBLE);
        mainGeoButton.setText(R.string.start_geofencing);
        title.setText(R.string.geofencing_ready);
        currentLayout = 2; // Update the current layout state
    }

    /**
     * Updates the UI to show the registration layout.
     * Animates the transition and updates the main button text and title.
     * Called when there is no existing geofencing information or when resetting.
     */
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

    /**
     * Displays a toast message to the user using the ToastHelper class.
     * @param message The message to be displayed in the toast.
     */
    public void showToast(String message) {
        ToastHelper.make(requireContext(), message, Toast.LENGTH_SHORT);
    }

    /**
     * Animates a view with a slide down animation.
     *
     * @param view The view to animate.
     * @param delay The delay before starting the animation in milliseconds.
     */
    public void slideAnimation(View view, int delay) {
        Animation slideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
        slideDown.setStartOffset(delay);
        view.startAnimation(slideDown);
    }

    /**
     * Animates a view with a fade animation.
     *
     * @param view The view to animate.
     * @param delay The delay before starting the
    animation in milliseconds.
     */
    public void fadeAnimation(View view, int delay) {
        Animation fade = AnimationUtils.loadAnimation(requireContext(), R.anim.fade);
        fade.setStartOffset(delay);
        view.startAnimation(fade);
    }

    /**
     * Checks the database for existing geofencing information.
     * Determines the UI state based on the retrieved data.
     * Shows the ready layout if geofencing is already set up; otherwise, shows the registration layout.
     */
    private void checkExistingGeofencingInfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    geofencingInfo = dataSnapshot.getValue(GeofencingInfo.class);
                    if (geofencingInfo != null && geofencingInfo.isOn()) {
                        currentLayout = 2;
                        showReadyLayout();
                        if(geofencingInfo.isOn())
                            handleStart();
                    } else {
                        showRegisterLayout();
                    }
                } else {
                    showRegisterLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Handles the process of resetting geofencing.
     * Validates the user's input and updates the database and UI accordingly.
     */
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
}