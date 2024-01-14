package com.cyk29.safewhere.reportmodule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.dataclasses.NotificationItem;
import com.cyk29.safewhere.dataclasses.Report;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * This class represents a BottomSheetDialogFragment used for making reports.
 * It allows users to report various types of incidents and send notifications.
 */
public class MakeReportFragment extends BottomSheetDialogFragment {
    private String type = "";
    private Button cancel, report;
    private TextView title, description;
    private EditText userDesc;
    private String userName, uid;
    private Location currentLocation;

    /**
     * Default constructor for MakeReportFragment.
     */
    public MakeReportFragment(){}

    /**
     * Constructor for MakeReportFragment with a specified report type.
     * @param type The type of report to be created.
     */
    public MakeReportFragment(String type){
        this.type = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLastLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_report, container, false);
        initializeUI(view);
        setUI();
        getUserName();
        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((ReportMainActivity) requireActivity()).clearDim();
    }

    /**
     * Initializes UI components and assigns their references.
     *
     * @param view The view containing UI components.
     */
    private void initializeUI(View view){
        cancel = view.findViewById(R.id.makeReport_cancel_Btn);
        report = view.findViewById(R.id.makeReport_report_Btn);
        title = view.findViewById(R.id.makeReport_title_TV);
        description = view.findViewById(R.id.makeReport_desc_TV);
        userDesc = view.findViewById(R.id.makeReport_userDesc_ET);
    }

    /**
     * Sets up the user interface and attaches click listeners to buttons.
     */
    private void setUI(){
        report.setOnClickListener(v -> {
            sendReport();
            requireActivity().finish();
        });
        cancel.setOnClickListener(v -> dismiss());
        switch (type){
            case "hazard":
                title.setText(formatString(getResources().getString(R.string.title_public_safety_hazards)));
                description.setText(getResources().getString(R.string.description_public_safety_hazards));
                break;
            case "suspicious":
                title.setText(formatString(getResources().getString(R.string.title_suspicious_activities)));
                description.setText(getResources().getString(R.string.description_suspicious_activities));
                break;
            case "vandalism":
                title.setText(formatString(getResources().getString(R.string.title_street_vandalism)));
                description.setText(getResources().getString(R.string.description_street_vandalism));
                break;
            case "envIssues":
                title.setText(formatString(getResources().getString(R.string.title_environmental_issues)));
                description.setText(getResources().getString(R.string.description_environmental_issues));
                break;
            case "animal":
                title.setText(formatString(getResources().getString(R.string.title_stray_animals)));
                description.setText(getResources().getString(R.string.description_stray_animals));
                break;
            case "accident":
                title.setText(formatString(getResources().getString(R.string.title_traffic_accidents)));
                description.setText(getResources().getString(R.string.description_traffic_accidents));
                break;
            case "health":
                title.setText(formatString(getResources().getString(R.string.title_public_health_concerns)));
                description.setText(getResources().getString(R.string.description_public_health_concerns));
                break;
            case "streetAlt":
                title.setText(formatString(getResources().getString(R.string.title_street_altercations)));
                description.setText(getResources().getString(R.string.description_street_altercations));
                break;
            case "violence":
                title.setText(formatString(getResources().getString(R.string.title_violence)));
                description.setText(getResources().getString(R.string.description_violence));
                break;
            default:
                title.setText(R.string.error);
                description.setText(R.string.error);
                break;
        }
    }

    /**
     * Sends a report to the Firebase Realtime Database.
     */
    private void sendReport() {
        uid = FirebaseAuth.getInstance().getUid();
        String type = title.getText().toString();
        String userDescription = userDesc.getText().toString();
        String date = String.valueOf(System.currentTimeMillis());
        String latitude;
        String longitude;
        if(currentLocation == null){
            ToastHelper.make(getContext(), "Wait for location Data", Toast.LENGTH_SHORT);
            getLastLocation();
            return;
        } else {
            latitude = String.valueOf(currentLocation.getLatitude());
            longitude = String.valueOf(currentLocation.getLongitude());
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports");
        String reportID = ref.push().getKey();
        Report report = new Report(reportID, uid, userName, type, userDescription, date, latitude, longitude);
        if (reportID != null) {
            ref.child(reportID).setValue(report);
        }
        sendNotification(uid,date);
        ToastHelper.make(getContext(), "Report Sent", Toast.LENGTH_SHORT);
    }

    /**
     * Formats a multi-line string by joining its lines into a single line.
     *
     * @param string The multi-line string to be formatted.
     * @return A single-line formatted string.
     */
    private String formatString(String string){
        String[] words = string.split("\n");
        return String.join(" ", words);
    }

    /**
     * Retrieves the user's name from Firebase based on their UID.
     */
    private void getUserName(){
        uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = null;
        if (uid != null) {
            ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
        }
        if (ref != null) {
            ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userName = Objects.requireNonNull(task.getResult().child("name").getValue()).toString();
                }
            });
        }
    }

    /**
     * Sends a notification to the user after a report has been successfully sent.
     *
     * @param uid  The UID of the user receiving the notification.
     * @param date The date of the report.
     */
    private void sendNotification(String uid, String date){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("notifications");
        String notifID = ref.push().getKey();
        NotificationItem notification = new NotificationItem(notifID, "Your report has been sent successfully.", formatDate(date), "report");
        if (notifID != null) {
            ref.child(notifID).setValue(notification);
        }
    }

    /**
     * Formats a date in milliseconds to a readable date string.
     *
     * @param dateInMillis The date in milliseconds to be formatted.
     * @return A formatted date string.
     */
    private String formatDate(String dateInMillis){
        long timeInMillis = Long.parseLong(dateInMillis);
        Date date = new Date(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Retrieves the last known location of the device.
     * Requires location permissions.
     */
    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> currentLocation = location);
    }
}