package com.cyk29.safewhere.startupmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.cyk29.safewhere.dataclasses.User;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    private Button resetPass, editProfile, logoutButton;
    private EditText name, phone, ecName, ecPhone, ecEmail;
    private boolean firstTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeUI(view);
        if(firstTime){
            customizeFirstTimeUI();
        } else {
            loadCredentials();
            setEditProfileClickListener();
        }
        setResetPassClickListener();
        setLogoutButtonClickListener();
        return view;
    }

    /**
     * Initialize UI elements.
     *
     * @param view The view of the fragment.
     */
    private void initializeUI(View view) {
        resetPass = view.findViewById(R.id.BTResetPassword);
        logoutButton = view.findViewById(R.id.BTLogout);
        editProfile = view.findViewById(R.id.BTEditProfile);
        name = view.findViewById(R.id.ETName);
        phone = view.findViewById(R.id.ETPhone);
        ecName = view.findViewById(R.id.ET_EC_name);
        ecPhone = view.findViewById(R.id.ET_EC_Phone);
        ecEmail = view.findViewById(R.id.ET_EC_Email);

        firstTime = requireActivity().getIntent().getBooleanExtra("firstTime", false);
    }

    /**
     * Customize UI elements for the first-time sign-up scenario.
     */
    private void customizeFirstTimeUI() {
        editProfile.setText(R.string.complete_profile);
        logoutButton.setVisibility(View.GONE);
        resetPass.setVisibility(View.GONE);
        resetPass.setEnabled(false);
        logoutButton.setEnabled(false);
        editProfile.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() ||
                    ecName.getText().toString().isEmpty() || ecPhone.getText().toString().isEmpty()) {
                ToastHelper.make(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
                return;
            }
            saveCredentials();
            startActivity(new Intent(getActivity(), MapsActivity.class));
            requireActivity().finish();
        });
    }

    /**
     * Set a click listener for the "Edit Profile" button.
     */
    private void setEditProfileClickListener() {
        editProfile.setOnClickListener(v -> {
            saveCredentials();
            ToastHelper.make(getContext(), "Profile updated", Toast.LENGTH_SHORT);
        });
    }

    /**
     * Set a click listener for the "Reset Password" button.
     */
    private void setResetPassClickListener() {
        resetPass.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVProfile, new ResetPasswordFragment())
                .addToBackStack(null)
                .commit());
    }

    /**
     * Set a click listener for the "Logout" button.
     */
    private void setLogoutButtonClickListener() {
        logoutButton.setOnClickListener(v -> {
            // Log out the user
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), StartupActivity.class));
            requireActivity().finish();
            // Clear saved credentials
            clearCredentials();
        });
    }

    /**
     * Saves profile information changes made by the user to the database.
     *
     */
    public void saveCredentials(){
        String userName = name.getText().toString();
        String userPhone = phone.getText().toString();
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String userEcName = ecName.getText().toString();
        String userEcPhone = ecPhone.getText().toString();
        String userEcEmail = ecEmail.getText().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(userName.isEmpty() || userPhone.isEmpty() || userEcName.isEmpty() || userEcPhone.isEmpty()){
            ToastHelper.make(getContext(), "Profile updated", Toast.LENGTH_SHORT);
            return;
        }
        User user = new User(uid,userName,userEmail,userPhone,userEcName,userEcPhone,userEcEmail);
        databaseReference.child(uid).setValue(user);
    }

    /**
     * Loads saved login credentials from database to allow user to manipulate the information.
     */
    public void loadCredentials() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        name.setText(user.getName());
                        phone.setText(user.getPhone());
                        ecName.setText(user.getEcName());
                        ecEmail.setText(user.getEcEmail());
                        ecPhone.setText(user.getEcPhone());
                    }
                } else {
                    ToastHelper.make(getContext(), "No data found for the current user", Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+databaseError.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";
    /**
     * Clears saved login credentials from shared preferences.
     */
    private void clearCredentials() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_EMAIL);
        editor.remove(PREF_PASSWORD);
        editor.apply();
    }
}