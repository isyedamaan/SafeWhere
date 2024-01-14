package com.cyk29.safewhere.startupmodule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.helperclasses.ToastHelper;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ResetPasswordFragment extends Fragment {

    private EditText oldPass, newPass, confPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        setupViews(view);
        return view;
    }

    /**
     * Sets up the views for this fragment.
     *
     * @param view The view associated with this fragment.
     */
    private void setupViews(View view) {
        oldPass = view.findViewById(R.id.oldPassET);
        newPass = view.findViewById(R.id.newPassET);
        confPass = view.findViewById(R.id.reenterNewPassET);
        Button reset = view.findViewById(R.id.BTResetPassword);
        reset.setOnClickListener(v -> handleResetButtonClick());
    }

    /**
     * Handles the click event of the reset button.
     */
    private void handleResetButtonClick() {
        String oldUserPass = oldPass.getText().toString();
        String newUserPass = newPass.getText().toString();
        String confUserPass = confPass.getText().toString();
        if (!newUserPass.equals(confUserPass)) {
            ToastHelper.make(requireActivity(), "Passwords do not match!", Toast.LENGTH_SHORT);
            return;
        }
        resetPassword(oldUserPass, newUserPass);
    }

    /**
     * Attempts to reset the user's password.
     *
     * @param oldPassword The old password entered by the user.
     * @param newPassword The new password entered by the user.
     */
    private void resetPassword(String oldPassword, final String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), oldPassword);
        reAuthenticateUser(user, credential, newPassword);
    }

    /**
     * Re-authenticates the user with the provided credentials.
     *
     * @param user The current user.
     * @param credential The authentication credential.
     * @param newPassword The new password.
     */
    private void reAuthenticateUser(FirebaseUser user, AuthCredential credential, String newPassword) {
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateUserPassword(newPassword);
                    } else {
                        ToastHelper.make(requireActivity(), "Re-authentication failed", Toast.LENGTH_SHORT);
                    }
                });
    }

    /**
     * Updates the user's password.
     *
     * @param newPassword The new password.
     */
    private void updateUserPassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        user.updatePassword(newPassword)
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        ToastHelper.make(requireActivity(), "Password updated successfully", Toast.LENGTH_SHORT);
                    } else {
                        ToastHelper.make(requireActivity(), "Failed to update password", Toast.LENGTH_SHORT);
                    }
                });
    }
}