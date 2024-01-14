package com.cyk29.safewhere.sosmodule;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyk29.safewhere.R;

/**
 * This class represents a fragment for panic mode in the SOS module. It provides a countdown timer
 * and UI elements for managing the panic mode.
 */
public class PanicModeFragment extends Fragment {

    private TextView countdownTextView;
    private ImageView continueButton;
    private Button cancel;
    private CountDownTimer countDownTimer;
    private boolean isGrey = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panicmode, container, false);
        initializeUI(view);
        timer();
        setUI();
        return view;
    }

    /**
     * Initializes UI components and assigns their references.
     *
     * @param view The view containing UI components.
     */
    private void initializeUI(View view){
        countdownTextView = view.findViewById(R.id.panic_countdown_TV);
        continueButton = view.findViewById(R.id.panic_continue_IV);
        cancel = view.findViewById(R.id.sos_cancel_BT);
    }

    /**
     * Sets up the user interface and attaches click listeners to buttons.
     */
    private void setUI(){
        continueButton.setOnClickListener(v -> {
            countDownTimer.cancel();
            timer();
        });
        cancel.setOnClickListener(v -> {
            startActivity(requireActivity().getIntent());
            requireActivity().finish();
        });
    }

    /**
     * Starts a countdown timer for panic mode.
     */
    private void timer(){
        countDownTimer = new CountDownTimer(10100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                countFinished();
                continueButton.setEnabled(false);
                AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
                if (parentActivity != null) {
                    if (parentActivity instanceof SosActivity) {
                        SosActivity myActivity = (SosActivity) parentActivity;
                        myActivity.alertContacts();
                    }
                }
            }
        }.start();
    }

    /**
     * Handles actions to be taken when the countdown timer finishes.
     */
    private void countFinished(){
        countdownTextView.setText(R.string.your_emergency_contacts_have_been_alerted);
        countdownTextView.setTextSize(25.0f);
        countdownTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isGrey) {
                    countdownTextView.setTextColor(Color.WHITE);
                } else {
                    countdownTextView.setTextColor(Color.GRAY);
                }
                isGrey = !isGrey;
                countdownTextView.postDelayed(this, 500); // Flash every 500 milliseconds
            }
        }, 0);
    }
}