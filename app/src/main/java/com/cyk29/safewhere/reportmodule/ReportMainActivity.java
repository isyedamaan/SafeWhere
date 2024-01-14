package com.cyk29.safewhere.reportmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cyk29.safewhere.R;

/**
 * This class represents the main activity for reporting incidents. It allows users to select
 * different types of incidents and report them through a BottomSheetDialogFragment.
 */
public class ReportMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);
        initializeUI();


    }

    /**
     * Displays a BottomSheetDialogFragment for making a report based on the selected type.
     *
     * @param type The type of incident to report.
     */
    private void makeOverlay(String type){
        MakeReportFragment makeReportFragment = new MakeReportFragment(type);
        dimBackground();
        makeReportFragment.show(getSupportFragmentManager(), "makeReportFragment");
    }

    /**
     * Initializes the user interface components and assigns click listeners to incident type buttons.
     */
    private void initializeUI(){
        ImageView hazard = findViewById(R.id.safetyHazardBT);
        ImageView suspicious = findViewById(R.id.susActivityBT);
        ImageView vandalism = findViewById(R.id.vandalismBT);
        ImageView envIssues = findViewById(R.id.envIssueBT);
        ImageView animal = findViewById(R.id.animalBT);
        ImageView accident = findViewById(R.id.accidentBT);
        ImageView health = findViewById(R.id.healthBT);
        ImageView streetAlt = findViewById(R.id.streetAltBT);
        ImageView violence = findViewById(R.id.violenceBT);
        ImageView back = findViewById(R.id.BackBtnReport);
        hazard.setOnClickListener(v -> makeOverlay("hazard"));
        suspicious.setOnClickListener(v -> makeOverlay("suspicious"));
        vandalism.setOnClickListener(v -> makeOverlay("vandalism"));
        envIssues.setOnClickListener(v -> makeOverlay("envIssues"));
        animal.setOnClickListener(v -> makeOverlay("animal"));
        accident.setOnClickListener(v -> makeOverlay("accident"));
        health.setOnClickListener(v -> makeOverlay("health"));
        streetAlt.setOnClickListener(v -> makeOverlay("streetAlt"));
        violence.setOnClickListener(v -> makeOverlay("violence"));
        back.setOnClickListener(v -> finish());
    }

    /**
     * Adds a semi-transparent overlay to dim the background when the BottomSheetDialogFragment is shown.
     */
    private void dimBackground() {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        Drawable dim = new ColorDrawable(Color.argb(128, 0, 0, 0));
        dim.setBounds(0, 0, rootView.getWidth(), rootView.getHeight());
        rootView.getOverlay().add(dim);
    }

    /**
     * Clears the dimming overlay from the background when the BottomSheetDialogFragment is dismissed.
     */
    public void clearDim() {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        rootView.getOverlay().clear();
    }
}