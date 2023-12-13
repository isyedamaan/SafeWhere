package com.cyk29.safewhere.startupmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.cyk29.safewhere.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFintro_signup);
        NavController navController = host.getNavController();
        
    }
}