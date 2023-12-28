package com.cyk29.safewhere.startupmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Button resetPass, editProfile, logoutButton;
    private EditText name, phone, ecName, ecPhone;
    private TextView email;
    private boolean firstTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize buttons
        resetPass = view.findViewById(R.id.BTResetPassword);
        logoutButton = view.findViewById(R.id.BTLogout);
        editProfile = view.findViewById(R.id.BTEditProfile);
        name = view.findViewById(R.id.ETName);
        phone = view.findViewById(R.id.ETPhone);
        ecName = view.findViewById(R.id.ET_EC_name);
        ecPhone = view.findViewById(R.id.ET_EC_Phone);
        email = view.findViewById(R.id.TV_Email);

        // can not edit email
        email.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Email cannot be changed", Toast.LENGTH_SHORT).show();
        });

        //if sign up
        firstTime = requireActivity().getIntent().getBooleanExtra("firstTime", false);
        if(firstTime)
        {
            editProfile.setText("Complete Profile");
            logoutButton.setVisibility(View.GONE);
            resetPass.setVisibility(View.GONE);
            resetPass.setEnabled(false);
            logoutButton.setEnabled(false);
            email.setText(requireActivity().getIntent().getStringExtra("email"));

            // Save credentials
            editProfile.setOnClickListener(v -> {
                saveCredentials();
                // Go to map
//                startActivity(new Intent(getActivity(), MapsActivity.class));
//                requireActivity().finish();
            });
        }

        resetPass.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.FCVProfile, new ResetPasswordFragment())
                .addToBackStack(null)
                .commit());

        logoutButton.setOnClickListener(v -> {
            // Log out the user
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish();
            // Clear saved credentials
            clearCredentials();
        });
        
        
        return view;
    }

    public void saveCredentials(){
        String userName = name.getText().toString();
        String userPhone = phone.getText().toString();
        String userEcName = ecName.getText().toString();
        String userEcPhone = ecPhone.getText().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(userName.isEmpty() || userPhone.isEmpty() || userEcName.isEmpty() || userEcPhone.isEmpty()){
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(uid, name.getText().toString(), phone.getText().toString(), ecName.getText().toString(), ecPhone.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.child("a").setValue(12, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
            }
        });
//        databaseReference.child("user_1").setValue(user, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                if (error == null) {
//                    Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Failed to save data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ProfileMainActivity) getActivity()).setBackButtonEnabled(!firstTime);
    }

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";
    private void clearCredentials() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_EMAIL);
        editor.remove(PREF_PASSWORD);
        editor.apply();
    }
}