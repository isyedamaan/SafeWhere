package com.cyk29.safewhere.startupmodule;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataObjects.User;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Button resetPass, editProfile, logoutButton;
    private EditText name, phone, ecName, ecPhone, ecEmail;
    private ImageView profile;
    private boolean firstTime;

    private static final int SELECT_IMAGE = 1;
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
        ecEmail = view.findViewById(R.id.ET_EC_Email);
        profile = view.findViewById(R.id.profilepicIV);


        //if sign up
        firstTime = requireActivity().getIntent().getBooleanExtra("firstTime", false);
        if(firstTime)
        {
            editProfile.setText("Complete Profile");
            logoutButton.setVisibility(View.GONE);
            resetPass.setVisibility(View.GONE);
            resetPass.setEnabled(false);
            logoutButton.setEnabled(false);

            // Save credentials
            editProfile.setOnClickListener(v -> {
                saveCredentials();
                // Go to map
                startActivity(new Intent(getActivity(), MapsActivity.class));
                requireActivity().finish();
            });
        } else {
            loadCredentials();
            //edit profile
            editProfile.setOnClickListener(v -> {
                saveCredentials();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            });
        }




        //profile pic
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });




        //resetPass
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



    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    public void saveCredentials(){
        String userName = name.getText().toString();
        String userPhone = phone.getText().toString();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userEcName = ecName.getText().toString();
        String userEcPhone = ecPhone.getText().toString();
        String userEcEmail = ecEmail.getText().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(userName.isEmpty() || userPhone.isEmpty() || userEcName.isEmpty() || userEcPhone.isEmpty()){
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(uid,userName,userEmail,userPhone,userEcName,userEcPhone,userEcEmail);
        databaseReference.child(uid).setValue(user);
    }

    public void loadCredentials() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Check if the UID is not null
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    // Now you can use the 'user' object to populate your UI fields or perform other actions
                    if (user != null) {
                        name.setText(user.getName());
                        phone.setText(user.getPhone());
                        ecName.setText(user.getEcName());
                        ecEmail.setText(user.getEcEmail());
                        ecPhone.setText(user.getEcPhone());
                    }
                } else {
                    // Handle the case where the user data does not exist
                    Toast.makeText(getContext(), "No data found for the current user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during the read operation
                Toast.makeText(getContext(), "Error loading data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
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