package com.cyk29.safewhere.mapmodule;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cyk29.safewhere.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

/**
 * DestinationSelectFragment is responsible for allowing the user to select a destination
 * for navigation purposes. This fragment uses Google Places API to provide an
 * autocomplete functionality for location search.
 */
public class DestinationSelectFragment extends Fragment {

    private static final String TAG = "DestinationSelectFragment";
    private AutocompleteSupportFragment autocompleteFragment;
    private LatLng latLng;
    private String placeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination_select, container, false);
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), getString(R.string.api_key));
        }
        setupAutocompleteFragment();
        setupStartButton(view);
        return view;
    }

    /**
     * Sets up the AutocompleteSupportFragment which provides the search bar
     * for inputting destination queries. It configures the fields required for the
     * place suggestions and sets up a listener for place selection.
     */
    private void setupAutocompleteFragment() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.home_autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,
                    Place.Field.NAME, Place.Field.LAT_LNG));
            customizeAutocompleteView();
            setAutocompleteListener();
        } else {
            Log.e(TAG, "AutocompleteSupportFragment is null");
        }
    }

    /**
     * Customizes the appearance of the autocomplete search bar, including the
     * search icon and input field. This enhances the UI to match the app's theme and style.
     */
    private void customizeAutocompleteView() {
        autocompleteFragment.requireView().post(() -> {
            customizeSearchIcon();
            customizeSearchInput();
        });
    }

    /**
     * Customizes the search icon in the autocomplete fragment.
     */
    private void customizeSearchIcon() {
        ImageView searchIcon = autocompleteFragment.requireView().findViewById
                (com.google.android.libraries.places.R.id.places_autocomplete_search_button);
        if (searchIcon != null) {
            searchIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_destionation_marker, null));
            int padding = (int) (15 * getResources().getDisplayMetrics().density) / 160;
            searchIcon.setPadding(padding, padding, padding, padding);
        }
    }

    /**
     * Customizes the search input in the autocomplete fragment.
     */
    private void customizeSearchInput() {
        EditText etPlace = autocompleteFragment.requireView().findViewById(com.google
                .android.libraries.places.R.id.places_autocomplete_search_input);
        if (etPlace != null) {
            etPlace.setHint(R.string.choose_destination);
            etPlace.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.lexend));
            etPlace.setTextSize(18f);
            etPlace.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            etPlace.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        }
    }

    /**
     * Sets the listener for place selection in the autocomplete fragment.
     */
    private void setAutocompleteListener() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLng = place.getLatLng();
                placeName = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "Error selecting place: " + status);
            }
        });
    }

    /**
     * Sets up the start navigation button. When clicked, it initiates the process
     * to navigate to the selected destination and changes the display fragment.
     * @param view The parent view of the fragment.
     */
    private void setupStartButton(View view) {
        Button startBtn = view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> startNavigation());
    }

    /**
     * Handles the start button click to begin navigation.
     */
    private void startNavigation() {
        if (latLng != null && placeName != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVHome, new OnRouteFragment(latLng, placeName)).commit();
        }
    }
}