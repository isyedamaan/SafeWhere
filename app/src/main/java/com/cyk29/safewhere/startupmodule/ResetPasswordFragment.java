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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordFragment extends Fragment {


    private EditText oldPass, newPass, confPass;
    private Button reset;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        oldPass = view.findViewById(R.id.oldPassET);
        newPass = view.findViewById(R.id.newPassET);
        confPass = view.findViewById(R.id.reenterNewPassET);

        reset = view.findViewById(R.id.BTResetPassword);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldUserPass = oldPass.getText().toString();
                String newUserPass = newPass.getText().toString();
                String confUserPass = confPass.getText().toString();

                if(newUserPass.equals(confUserPass)){
                    resetPassword(oldUserPass, newUserPass);
                } else {
                    Toast.makeText(requireActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private void resetPassword(String oldPassword, final String newPassword) {
        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Create a credential with the current email and password
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

        // Re-authenticate the user with the provided credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Re-authentication successful, now update the password
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        // Password updated successfully
                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Handle password update failure
                                        Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Handle re-authentication failure
                        Toast.makeText(getContext(), "Re-authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}