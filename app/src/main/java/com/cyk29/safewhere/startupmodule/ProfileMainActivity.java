package com.cyk29.safewhere.startupmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cyk29.safewhere.R;

public class ProfileMainActivity extends AppCompatActivity {

    ImageView backBtn;
    Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backBtn = findViewById(R.id.IVBackProfile);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });




    }



}
