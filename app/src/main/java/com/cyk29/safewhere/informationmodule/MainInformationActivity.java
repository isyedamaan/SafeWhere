package com.cyk29.safewhere.informationmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyk29.safewhere.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

/**
 * MainInformationActivity is responsible for managing the main information screen of the app.
 * It includes navigation controls and formatting for the bottom navigation menu.
 */
public class MainInformationActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_information);
        initializeUI();
    }

    /**
     * Initialize UI components and set up navigation.
     */
    private void initializeUI() {
        ImageView btnBack = findViewById(R.id.info_back_Btn);
        btnBack.setOnClickListener(v -> finish());

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.info_FCV);
        NavController navController = Objects.requireNonNull(host).getNavController();

        setupBottomNavMenu(navController);
        formatMenuUI();
    }

    /**
     * Set up the bottom navigation menu.
     * @param navController The NavController for the activity.
     */
    private void setupBottomNavMenu(NavController navController) {
        bottomNavigationView = findViewById(R.id.info_bottom_nav_bar);
        NavigationUI.setupWithNavController(MainInformationActivity.this.bottomNavigationView, navController);
    }

    /**
     * Format the UI elements of the bottom navigation menu.
     */
    private void formatMenuUI() {
        Typeface typeface = ResourcesCompat.getFont(this, R.font.lexend_bold);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            formatIconView(itemView);
            formatTextView(itemView, com.google.android.material.R.id.navigation_bar_item_small_label_view, typeface, 15);
            formatTextView(itemView, com.google.android.material.R.id.navigation_bar_item_large_label_view, typeface, 17);
        }
    }

    /**
     * Format the icon view of the bottom navigation item.
     * @param itemView The BottomNavigationItemView to format.
     */
    private void formatIconView(BottomNavigationItemView itemView) {
        ImageView iconView = itemView.findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view);
        iconView.setScaleX(1.1f);
        iconView.setScaleY(1.1f);
    }

    /**
     * Format the text view of the bottom navigation item.
     * @param itemView The BottomNavigationItemView containing the text view.
     * @param textId The ID of the TextView within the item view.
     * @param typeface The Typeface to apply to the TextView.
     * @param textSize The size of the text.
     */
    private void formatTextView(BottomNavigationItemView itemView, int textId, Typeface typeface, float textSize) {
        View textView = itemView.findViewById(textId);
        if (textView instanceof TextView) {
            TextView text = (TextView) textView;
            text.setTypeface(typeface);
            text.setTextSize(textSize);
        }
    }
}

