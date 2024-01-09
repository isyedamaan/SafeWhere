package com.cyk29.safewhere.mapmodule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.informationmodule.MainInformationActivity;
import com.cyk29.safewhere.mapmodule.geofencing.GeofencingFragment;
import com.cyk29.safewhere.notificationmodule.NotificationMainActivity;
import com.cyk29.safewhere.reportmodule.ReportMainActivity;
import com.cyk29.safewhere.sosmodule.SosActivity;
import com.cyk29.safewhere.startupmodule.ProfileMainActivity;

public class HomeFragment extends Fragment {
    public HomeFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button startBtn = view.findViewById(R.id.chooseDestBtn);
        startBtn.setOnClickListener(v -> {
            // Navigate to Fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVHome, new DestinationSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        ImageView sos = view.findViewById(R.id.mainSOSBtn);
        sos.setOnClickListener(v -> startActivity(new Intent(requireContext(), SosActivity.class)));


        Button infoBtn = view.findViewById(R.id.infoSlideBtn);
        infoBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), MainInformationActivity.class)));

        Button repBtn = view.findViewById(R.id.reportSlideBtn);
        repBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), ReportMainActivity.class)));

        ImageView notifBtn = view.findViewById(R.id.notifBtn);
        notifBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), NotificationMainActivity.class)));

        Button geoBtn = view.findViewById(R.id.geoSlideBtn);
        geoBtn.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, new GeofencingFragment())
                .addToBackStack(null)
                .commit());

        ImageView profile = view.findViewById(R.id.profileIVBtn);
        profile.setOnClickListener(v -> startActivity(new Intent(requireContext(), ProfileMainActivity.class)));

        return view;
    }

}