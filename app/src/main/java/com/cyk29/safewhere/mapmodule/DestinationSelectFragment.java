package com.cyk29.safewhere.mapmodule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyk29.safewhere.R;


public class DestinationSelectFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_destination_select, container, false);

        Button startBtn = view.findViewById(R.id.startBtn);
         startBtn.setOnClickListener(v -> {
                    // Navigate to Fragment2
             getParentFragmentManager().beginTransaction()
                     .replace(R.id.FCVHome, new OnRouteFragment())
                     .addToBackStack(null)
                     .commit();
         });
         return view;
    }
}