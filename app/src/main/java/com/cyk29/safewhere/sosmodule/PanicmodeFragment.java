package com.cyk29.safewhere.sosmodule;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyk29.safewhere.R;

public class PanicmodeFragment extends Fragment {

    private TextView countdownTextView;
    private ImageView continueButton;
    private CountDownTimer countDownTimer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panicmode, container, false);

        countdownTextView = view.findViewById(R.id.countdown);
        continueButton = view.findViewById(R.id.panicContinueIV);

        timer();
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                timer();
            }
        });

        return view;
    }

    private void timer(){
        countDownTimer = new CountDownTimer(10100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining time
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Countdown finished, you can perform any action here
                countFinished();
                continueButton.setEnabled(false);
                AppCompatActivity parentActivity = (AppCompatActivity) getActivity();

                //call mothod from parent activity
                if (parentActivity != null) {
                    if (parentActivity instanceof SosActivity) {
                        SosActivity myActivity = (SosActivity) parentActivity;
                        myActivity.alertContacts();
                    }
                };
            }
        }.start();
    }


    private Handler handler;
    private boolean isRed = true;
    private void countFinished(){
        countdownTextView.setText("Your Emergency Contacts have been alerted.");
        countdownTextView.setTextSize(25.0f);
        countdownTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Toggle text color between red and white
                if (isRed) {
                    countdownTextView.setTextColor(Color.WHITE);
                } else {
                    countdownTextView.setTextColor(Color.RED);
                }
                isRed = !isRed;

                // Call the method recursively after a delay
                countdownTextView.postDelayed(this, 500); // Flash every 500 milliseconds
            }
        }, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks

        countdownTextView.setText("Your Emergency Contacts have been alerted.");
        countdownTextView.setTextSize(25.0f);
        countdownTextView.removeCallbacks(null);
    }
}