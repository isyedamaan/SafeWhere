package com.cyk29.safewhere.informationmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.FirstFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainInformationActivity extends AppCompatActivity{


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


}