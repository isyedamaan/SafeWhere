package com.cyk29.safewhere.startupmodule;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.google.firebase.FirebaseApp;

public class ProfileMainActivity extends AppCompatActivity {

    private boolean firstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeUI();
        FirebaseApp.initializeApp(this);
        handleFirstTime();

    }

    /**
     * Initialize UI elements.
     */
    private void initializeUI() {
        ImageView backBtn = findViewById(R.id.IVBackProfile);
        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }


    /**
     * Handles the back button visibility for the profile activity.
     */
    private void handleFirstTime() {
        firstTime = getIntent().getBooleanExtra("firstTime", false);
        handleBackButton();
    }

    /**
     * Handle how the back button works in different scenarios.
     */
    private void handleBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               if(!firstTime)
                   finish();
               else {
                   ToastHelper.make(getApplicationContext(), "Please complete your profile first!", Toast.LENGTH_SHORT);
               }
            }
        });
    }

}
