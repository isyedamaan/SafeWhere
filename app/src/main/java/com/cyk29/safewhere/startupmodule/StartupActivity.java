package com.cyk29.safewhere.startupmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StartupActivity extends AppCompatActivity {
    private static final String TAG = "StartupActivity";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        loadCredentials();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermissions();
    }

    /**
     * Handles Firebase authentication for login.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
    public void firebaseLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        ToastHelper.make(getApplicationContext(), "Welcome Back!", Toast.LENGTH_SHORT);
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        finish();
                    } else {
                        ToastHelper.make(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT);
                    }
                });
    }

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    /**
     * Loads saved login credentials from shared preferences and attempts auto-login.
     */
    private void loadCredentials() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = prefs.getString(PREF_EMAIL, "");
        String savedPassword = prefs.getString(PREF_PASSWORD, "");
        if (savedEmail.isEmpty() || savedPassword.isEmpty()) {
            return;
        }
        firebaseLogin(savedEmail, savedPassword);
    }


    /**
     * Requests location permissions from the user.
     */
    private void askPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12001);
    }

    /**
     * Handles the result of permission requests.
     *
     * @param requestCode  The code of the requested permission.
     * @param permissions  The requested permissions.
     * @param grantResults The results of the permission requests.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 12001 && grantResults.length > 0)
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                makeAlert();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 12002);
                }
            }
        if(requestCode == 12002 && grantResults.length > 0)
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                makeAlert();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 12003);
                }
            }
        if(requestCode == 12003 && grantResults.length > 0)
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                makeAlert();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 12004);
                }
            }
        if(requestCode == 12004 && grantResults.length > 0)
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                makeAlert();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 12005);
                }
            }
    }


    /**
     * Shows an alert dialog to inform the user about required permissions.
     */
    private void makeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions");
        builder.setMessage("Permissions are required for this app to function as our app revolves around providing users with danger and report information based on their location.\n");
        builder.setPositiveButton("OK", (dialog, which) -> {
            openAppSettings();
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> finish());
        builder.setIcon(R.drawable.app_logo);
        builder.show();
    }

    /**
     * Opens the app settings in the device's settings app.
     */
    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(android.net.Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}