package com.cyk29.safewhere.reportmodule;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.NotificationItem;
import com.cyk29.safewhere.dataclasses.Report;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MakeReportFragment extends BottomSheetDialogFragment {
    private String type = "";
    public MakeReportFragment(){
    }
    public MakeReportFragment(String type){
        this.type = type;
    }
    private Button cancel, report;
    private TextView title, description;
    private EditText userDesc;
    private String userName, uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_report, container, false);
        initializeUI(view);
        setUI();
        getUserName();
        return view;
    }

    private void initializeUI(View view){
        cancel = view.findViewById(R.id.cancelReportBT);
        report = view.findViewById(R.id.reportBT);
        title = view.findViewById(R.id.reportTitleFragTV);
        description = view.findViewById(R.id.reportDescTV);
        userDesc = view.findViewById(R.id.userDescriptionET);
    }

    private void setUI(){
        report.setOnClickListener(v -> {
            sendReport();
            requireActivity().finish();
        });
        cancel.setOnClickListener(v -> {dismiss();});
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

    private void sendReport() {
        uid = FirebaseAuth.getInstance().getUid();
        String type = title.getText().toString();
        String userDescription = userDesc.getText().toString();
        String date = String.valueOf(System.currentTimeMillis());
        // TODO connect to location services
        String latitude = "0";
        String longitude = "0";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports");
        String reportID = ref.push().getKey();
        Report report = new Report(reportID, uid, userName, type, userDescription, date, latitude, longitude);
        if (reportID != null) {
            ref.child(reportID).setValue(report);
        }
        sendNotification(uid,date);
        Toast.makeText(getContext(), "Report Sent", Toast.LENGTH_SHORT).show();
    }

    private String formatString(String string){
        String[] words = string.split("\n");
        return String.join(" ", words);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((ReportMainActivity) requireActivity()).clearDim();
    }

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

    private void sendNotification(String uid, String date){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("notifications");
        String notifID = ref.push().getKey();
        NotificationItem notification = new NotificationItem(notifID, "Your report has been sent successfully.", formatDate(date), "report");
        if (notifID != null) {
            ref.child(notifID).setValue(notification);
        }
    }

    private String formatDate(String dateInMillis){
        long timeInMillis = Long.parseLong(dateInMillis);
        Date date = new Date(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}