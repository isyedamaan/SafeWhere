package com.cyk29.safewhere.mapmodule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyk29.safewhere.R;

public class OnRouteFragment extends Fragment {
    Button endBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_route, container, false);

        endBtn = view.findViewById(R.id.endBtn);
        endBtn.setOnClickListener(v -> {
            if (getActivity() instanceof MapsActivity) {
                ((MapsActivity) getActivity()).clearBackStack();
            }
            // Navigate to Fragment2
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVHome, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }
}