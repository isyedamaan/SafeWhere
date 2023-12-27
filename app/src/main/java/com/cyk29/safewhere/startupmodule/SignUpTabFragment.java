package com.cyk29.safewhere.startupmodule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.MapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpTabFragment extends Fragment {



    EditText emailET, passwordET, confirmPasswordET;
    String email, password, confirmPassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
        emailET = view.findViewById(R.id.SUEmailET);
        passwordET = view.findViewById(R.id.SUPasswordET);
        confirmPasswordET = view.findViewById(R.id.SUConfirmPasswordET);



        return view;
    }


    Button signupBtn;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        signupBtn = view.findViewById(R.id.signupBT);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                confirmPassword = confirmPasswordET.getText().toString();
                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals(confirmPassword))
                    firebaseSignUp(email, password);
                else
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public void firebaseSignUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getContext(), ProfileMainActivity.class));
                            Toast.makeText(getContext(), "Before proceeding, please Enter details.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Sign up failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

