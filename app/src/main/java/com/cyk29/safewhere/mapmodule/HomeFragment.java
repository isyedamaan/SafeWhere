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

    private Button startBtn;
    private ImageView sos;
    private Button infoBtn;
    private Button repBtn;
    private ImageView notificationsButton;
    private Button geoBtn;
    private ImageView profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeUI(view);
        setUI();
        return view;
    }

    /**
     * Initializes UI components from the XML layout.
     * @param view The root view of the fragment's layout, used to find each UI component.
     */
    private void initializeUI(View view){
        startBtn = view.findViewById(R.id.chooseDestBtn);
        sos = view.findViewById(R.id.mainSOSBtn);
        infoBtn = view.findViewById(R.id.infoSlideBtn);
        repBtn = view.findViewById(R.id.reportSlideBtn);
        notificationsButton = view.findViewById(R.id.notifBtn);
        geoBtn = view.findViewById(R.id.geoSlideBtn);
        profile = view.findViewById(R.id.profileIVBtn);
    }

    /**
     * Sets up the UI interaction elements of the fragment.
     * Defines the behavior for button clicks, including navigation to different activities or fragments.
     */
    private void setUI(){
        startBtn.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, new DestinationSelectFragment())
                .addToBackStack(null)
                .commit());
        sos.setOnClickListener(v -> startActivity(new Intent(requireContext(), SosActivity.class)));
        repBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), ReportMainActivity.class)));
        notificationsButton.setOnClickListener(v -> startActivity(new Intent(requireContext(), NotificationMainActivity.class)));
        infoBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), MainInformationActivity.class)));
        geoBtn.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVHome, new GeofencingFragment())
                .addToBackStack(null)
                .commit());
        profile.setOnClickListener(v -> startActivity(new Intent(requireContext(), ProfileMainActivity.class)));
    }
}