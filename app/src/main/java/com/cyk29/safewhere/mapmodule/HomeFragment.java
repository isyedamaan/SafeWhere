package com.cyk29.safewhere.mapmodule;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.informationmodule.MainInformationActivity;
import com.cyk29.safewhere.mapmodule.geofencing.GeofencingActivity;
import com.cyk29.safewhere.notificationmodule.NotificationMainActivity;
import com.cyk29.safewhere.reportmodule.ReportMainActivity;
import com.cyk29.safewhere.sosmodule.SosActivity;
import com.cyk29.safewhere.startupmodule.ProfileMainActivity;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    private HorizontalScrollView horizontalScrollView;
    private Button startBtn;
    private ImageView sos, notifBtn, profile;
    private View report;
    private Button infoBtn, repBtn, geoBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        horizontalScrollView = view.findViewById(R.id.HCVbtns);
        startBtn = view.findViewById(R.id.chooseDestBtn);
        startBtn.setOnClickListener(v -> {
            // Navigate to Fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVHome, new DestinationSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        sos = view.findViewById(R.id.mainSOSBtn);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity().getApplicationContext(), SosActivity.class);
                startActivity(intent);
            }
        });



        infoBtn = view.findViewById(R.id.infoSlideBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity().getApplicationContext(), MainInformationActivity.class);
                startActivity(intent);
            }
        });

        repBtn = view.findViewById(R.id.reportSlideBtn);
        repBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity().getApplicationContext(), ReportMainActivity.class);
                startActivity(intent);
            }
        });

        notifBtn = view.findViewById(R.id.notifBtn);
        notifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity().getApplicationContext(), NotificationMainActivity.class);
                startActivity(i);
            }
        });

        geoBtn = view.findViewById(R.id.geoSlideBtn);
        geoBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity().getApplicationContext(), GeofencingActivity.class));
        });

        profile = view.findViewById(R.id.profileIVBtn);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity().getApplicationContext(), ProfileMainActivity.class);
            startActivity(intent);
        });


        return view;
    }

}