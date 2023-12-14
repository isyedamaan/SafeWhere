package com.cyk29.safewhere.reportmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.MapsActivity;

public class ReportMainActivity extends AppCompatActivity {

    ImageView murder, back;

    View dark;
    FragmentContainerView FCV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);

        dark = findViewById(R.id.dark);
        FCV = findViewById(R.id.FCVReport);

        murder = findViewById(R.id.murderBT);
        murder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dark.setVisibility(View.VISIBLE);
                FCV.setVisibility(View.VISIBLE);
            }
        });

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGone();
            }
        });

        back = findViewById(R.id.BackBtnReport);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    void setGone(){
        dark.setVisibility(View.GONE);
        FCV.setVisibility(View.GONE);
    }
}