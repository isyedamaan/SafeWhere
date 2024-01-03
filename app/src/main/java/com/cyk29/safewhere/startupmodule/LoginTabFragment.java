package com.cyk29.safewhere.startupmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {

    private Handler handler;
    private TextView forgotPassTV;
    private RadioButton radioButton;

    private Button loginBtn;
    public EditText emailET, passwordET;

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        emailET = view.findViewById(R.id.loginEmailET);
        passwordET = view.findViewById(R.id.loginPasswordET);

        //FORGOT PASSWORD
        forgotPassTV = view.findViewById(R.id.ForgotPassTV);
        handler = new Handler();
        forgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change color when clicked
                forgotPassTV.setAlpha(0.7f);
                // Revert back to the original color after a delay (e.g., 1000 milliseconds or 1 second)
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forgotPassTV.setAlpha(1f);
                    }
                }, 500);

                // Send reset email
                String email = emailET.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Reset Email sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Email not found", Toast.LENGTH_SHORT).show();
                                }
                                }
                            });
                }

            }
        });


        //Remember me button
        radioButton = view.findViewById(R.id.radioBT);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioButton.isSelected()) {
                    radioButton.setChecked(true);
                    radioButton.setSelected(true);
                    radioButton.setAlpha(1f);
                } else {
                    radioButton.setChecked(false);
                    radioButton.setSelected(false);
                    radioButton.setAlpha(0.5f);
                }
            }
        });

        //LOGIN BUTTON
        loginBtn = view.findViewById(R.id.loginBT);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty())
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else {
                    firebaseLogin(email, password);
                    if (radioButton.isChecked()) {
                        saveCredentials(email, password);
                    }
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_tab, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        loadCredentials();
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public void firebaseLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getContext(), MapsActivity.class));
                            Toast.makeText(getContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";


    private void saveCredentials(String email, String password) {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public void loadCredentials() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = prefs.getString(PREF_EMAIL, "");
        String savedPassword = prefs.getString(PREF_PASSWORD, "");
        if(savedEmail.isEmpty() || savedPassword.isEmpty())
            return;
        firebaseLogin(savedEmail, savedPassword);
    }

}