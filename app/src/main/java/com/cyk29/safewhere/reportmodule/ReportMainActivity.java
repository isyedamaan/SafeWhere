package com.cyk29.safewhere.reportmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cyk29.safewhere.R;

public class ReportMainActivity extends AppCompatActivity {

    ImageButton hazard, suspicious, vandalism,envIssues,animal,accident,health,streetAlt,violence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);
        initializeUI();

        hazard.setOnClickListener(v -> {makeOverlay("hazard");});
        suspicious.setOnClickListener(v -> {makeOverlay("suspicious");});
        vandalism.setOnClickListener(v -> {makeOverlay("vandalism");});
        envIssues.setOnClickListener(v -> {makeOverlay("envIssues");});
        animal.setOnClickListener(v -> {makeOverlay("animal");});
        accident.setOnClickListener(v -> {makeOverlay("accident");});
        health.setOnClickListener(v -> {makeOverlay("health");});
        streetAlt.setOnClickListener(v -> {makeOverlay("streetAlt");});
        violence.setOnClickListener(v -> {makeOverlay("violence");});
    }

    private void makeOverlay(String type){
        MakeReportFragment makeReportFragment = new MakeReportFragment(type);
        dimBackground();
        makeReportFragment.show(getSupportFragmentManager(), "makeReportFragment");
    }

    private void initializeUI(){
        hazard = findViewById(R.id.safetyHazardBT);
        suspicious = findViewById(R.id.susActivityBT);
        vandalism = findViewById(R.id.vandalismBT);
        envIssues = findViewById(R.id.envIssueBT);
        animal = findViewById(R.id.animalBT);
        accident = findViewById(R.id.accidentBT);
        health = findViewById(R.id.healthBT);
        streetAlt = findViewById(R.id.streetAltBT);
        violence = findViewById(R.id.violenceBT);
    }

    private void dimBackground() {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        Drawable dim = new ColorDrawable(Color.argb(128, 0, 0, 0));
        dim.setBounds(0, 0, rootView.getWidth(), rootView.getHeight());
        rootView.getOverlay().add(dim);
    }
    public void clearDim() {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        rootView.getOverlay().clear();
    }
}