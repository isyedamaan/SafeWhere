package com.cyk29.safewhere.startupmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This fragment represents the login tab of the startup module.
 * It allows users to enter their email and password to log in.
 * It also provides a button to reset their password if they have forgotten it.
 *
 */
public class LoginTabFragment extends Fragment {
    private Handler handler;
    private TextView forgotPassTV;
    private RadioButton radioButton;
    public EditText emailET, passwordET;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        emailET = view.findViewById(R.id.loginEmailET);
        passwordET = view.findViewById(R.id.loginPasswordET);
        setUpForgotPassword(view);
        setUpRememberMe(view);
        setUpLoginButton(view);
    }

    /**
     * Sets up the "Forgot Password" functionality and related actions.
     *
     * @param view The fragment's root view.
     */
    private void setUpForgotPassword(View view) {
        forgotPassTV = view.findViewById(R.id.ForgotPassTV);
        handler = new Handler();
        forgotPassTV.setOnClickListener(v -> handleForgotPasswordClick());
    }

    /**
     * Handles the click event for the "Forgot Password" text view.
     */
    private void handleForgotPasswordClick() {
        forgotPassTV.setAlpha(0.7f);
        handler.postDelayed(() -> forgotPassTV.setAlpha(1f), 500);

        String email = emailET.getText().toString().trim();
        if (email.isEmpty()) {
            ToastHelper.make(getContext(), "Please enter your email", Toast.LENGTH_SHORT);
        } else {
            sendPasswordResetEmail(email);
        }
    }

    /**
     * Sends a password reset email to the specified email address.
     *
     * @param email The user's email address.
     */
    private void sendPasswordResetEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ToastHelper.make(getContext(), "Reset Email sent", Toast.LENGTH_SHORT);
                    } else {
                        ToastHelper.make(getContext(), "Email not found", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void setUpRememberMe(View view) {
        radioButton = view.findViewById(R.id.radioBT);
        radioButton.setOnClickListener(v -> {
            if (!radioButton.isSelected()) {
                radioButton.setChecked(true);
                radioButton.setSelected(true);
                radioButton.setAlpha(1f);
            } else {
                radioButton.setChecked(false);
                radioButton.setSelected(false);
                radioButton.setAlpha(0.5f);
            }
        });
    }

    /**
     * Sets up the login button functionality and related actions.
     *
     * @param view The fragment's root view.
     */
    private void setUpLoginButton(View view) {
        Button loginBtn = view.findViewById(R.id.loginBT);
        loginBtn.setOnClickListener(v -> handleLoginButtonClick());
    }

    /**
     * Handles the click event for the login button.
     */
    private void handleLoginButtonClick() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            ToastHelper.make(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
        } else {
            firebaseLogin(email, password);
            if (radioButton.isChecked()) {
                saveCredentials(email, password);
            }
        }
    }

    /**
     * Performs login using Firebase Authentication.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
    public void firebaseLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getContext(), MapsActivity.class));
                        ToastHelper.make(getContext(), "Welcome Back!", Toast.LENGTH_SHORT);
                    } else {
                        ToastHelper.make(getContext(), "Authentication failed.", Toast.LENGTH_SHORT);
                    }
                });
    }

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";
    /**
     * Saves login credentials to shared preferences.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
    private void saveCredentials(String email, String password) {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }
}