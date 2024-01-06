package com.cyk29.safewhere.informationmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
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
                Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
                startActivity(intent);
            }
        });

        //BACK BUTTON
        ImageView BtnBack = findViewById(R.id.backInfoBT);
        View.OnClickListener OCLback = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        BtnBack.setOnClickListener(OCLback);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.FCVInfo);
        NavController navController = host.getNavController();

        setupBottomNavMenu(navController);
        formatMenuUI(bottomNavigationView);
    }

        private void setupBottomNavMenu(NavController navController) {
            bottomNavigationView = findViewById(R.id.infoBottomNavBar);
            NavigationUI.setupWithNavController(MainInformationActivity.this.bottomNavigationView, navController);
        }

    public void formatMenuUI(BottomNavigationView bottomNavigationView) {
        Typeface typeface = ResourcesCompat.getFont(this, R.font.lexend_bold);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);

            ImageView iconView = itemView.findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view);
            iconView.setScaleX(1.1f);
            iconView.setScaleY(1.1f);

            View smallItemText = itemView.findViewById(com.google.android.material.R.id.navigation_bar_item_small_label_view);
            if (smallItemText instanceof TextView) {
                TextView t = ((TextView) smallItemText);
                t.setTypeface(typeface);
                t.setTextSize(15);
            }
            View largeItemText = itemView.findViewById(com.google.android.material.R.id.navigation_bar_item_large_label_view);
            if (largeItemText instanceof TextView) {
                TextView t = ((TextView) largeItemText);
                t.setTypeface(typeface);
                t.setTextSize(17);
            }
        }
    }


}