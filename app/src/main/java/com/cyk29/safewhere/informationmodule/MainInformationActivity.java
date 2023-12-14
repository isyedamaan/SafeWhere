package com.cyk29.safewhere.informationmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.FirstFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainInformationActivity extends AppCompatActivity{


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_information);

        // In the source module
        ImageView backBtn = findViewById(R.id.backInfoBT);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the target module
                Intent intent = new Intent(getApplicationContext(), FirstFragment.class);
                startActivity(intent);
            }
        });
/*
        //BACK BUTTON
        ImageView BtnBack = findViewById(R.id.backInfoBT);
        View.OnClickListener OCLback = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestIntro);
            }
        };
        BtnBack.setOnClickListener(OCLback);*/

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.FCVInfo);
        NavController navController = host.getNavController();

        setupBottomNavMenu(navController);
    }

        private void setupBottomNavMenu(NavController navController) {
            BottomNavigationView bottomNav = findViewById(R.id.infoBottomNavBar);
            NavigationUI.setupWithNavController(bottomNav, navController);

        }
}