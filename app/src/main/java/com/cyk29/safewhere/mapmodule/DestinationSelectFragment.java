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


public class DestinationSelectFragment extends Fragment {

    private AutocompleteSupportFragment autocompleteFragment;
    private LatLng latLng;
    private String placeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_destination_select, container, false);

        if(!Places.isInitialized())
            Places.initialize(requireActivity().getApplicationContext(), getString(R.string.api_key));

        setupAutocompleteFragment();
        Button startBtn = view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, new OnRouteFragment(latLng, placeName)).commit());
        return view;
    }

    private void setupAutocompleteFragment() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.home_autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        } else {
            Log.e("AutocompleteError", "AutocompleteSupportFragment is null");
        }
        if (autocompleteFragment != null) {
            autocompleteFragment.requireView().post(() -> {
                ImageView searchIcon = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_button);
                if (searchIcon != null) {
                    searchIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_destionation_marker, null));
                    searchIcon.setMaxWidth(40);
                    int padding = (int) (15 * getResources().getDisplayMetrics().density)/160;
                    searchIcon.setPadding(padding, padding, padding, padding);
                }

                EditText etPlace = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                // Set the hint to empty or to a new placeholder text
                etPlace.setHint(R.string.choose_destination);
                etPlace.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.lexend));
                etPlace.setTextSize(18f);
                etPlace.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
                etPlace.setHintTextColor(ContextCompat.getColor(requireContext(),R.color.grey));
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
}