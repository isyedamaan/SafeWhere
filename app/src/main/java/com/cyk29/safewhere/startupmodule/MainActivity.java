package com.cyk29.safewhere.startupmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.GeofencingInfo;
import com.cyk29.safewhere.informationmodule.MainInformationActivity;
import com.cyk29.safewhere.mapmodule.GeofencingActivity;
import com.cyk29.safewhere.mapmodule.GeofencingOnActivity;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        loadCredentials();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFintro_signup);
        NavController navController = host.getNavController();

    }


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public void firebaseLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                            Toast.makeText(getApplicationContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";
    public void loadCredentials() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = prefs.getString(PREF_EMAIL, "");
        String savedPassword = prefs.getString(PREF_PASSWORD, "");
        if(savedEmail.isEmpty() || savedPassword.isEmpty()){
            return;
        }
        firebaseLogin(savedEmail, savedPassword);
    }
}