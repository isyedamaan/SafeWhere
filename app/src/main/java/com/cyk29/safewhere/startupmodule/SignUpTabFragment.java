package com.cyk29.safewhere.startupmodule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SignUpTabFragment extends Fragment {
    private static final String TAG = "SignUpTabFragment";
    private EditText emailET, passwordET, confirmPasswordET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
        initializeUI(view);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button signupBtn = view.findViewById(R.id.signupBT);
        signupBtn.setOnClickListener(v -> validateInputFields());
    }


    /**
     * Initializes the UI components.
     *
     * @param view The view associated with this fragment.
     */
    private void initializeUI(View view) {
        emailET = view.findViewById(R.id.SUEmailET);
        passwordET = view.findViewById(R.id.SUPasswordET);
        confirmPasswordET = view.findViewById(R.id.SUConfirmPasswordET);
    }

    /**
     * Validates the input fields.
     */
    private void validateInputFields() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            ToastHelper.make(requireActivity(), "Please fill in all fields", Toast.LENGTH_SHORT);
            return;
        }

        if (password.equals(confirmPassword)) {
            createUserWithEmailAndPassword(email, password);
        } else {
            ToastHelper.make(requireActivity(), "Passwords do not match!", Toast.LENGTH_SHORT);
        }
    }


    /**
     * Creates a new user account using the provided email address and password.
     *
     * @param email The email address.
     * @param password The password.
     */
    private void createUserWithEmailAndPassword(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        navigateToProfileActivity(email);
                    } else {
                        ToastHelper.make(requireActivity(), "Authentication failed.", Toast.LENGTH_SHORT);
                        Log.e(TAG, "createUserWithEmailAndPassword: "+ Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                });
    }

    /**
     * Navigates to the profile activity.
     *
     * @param email The email address of the user.
     */
    private void navigateToProfileActivity(String email) {
        Intent i = new Intent(getContext(), ProfileMainActivity.class);
        i.putExtra("firstTime", true);
        i.putExtra("email", email);
        startActivity(i);
        ToastHelper.make(getContext(), "Before proceeding, please Enter details", Toast.LENGTH_SHORT);
        requireActivity().finish();
    }
}

