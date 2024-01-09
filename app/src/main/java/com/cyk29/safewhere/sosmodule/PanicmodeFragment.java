package com.cyk29.safewhere.sosmodule;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyk29.safewhere.R;

public class PanicmodeFragment extends Fragment {

    private TextView countdownTextView;
    private ImageView continueButton;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer1, mediaPlayer2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer1 = MediaPlayer.create(getContext(), R.raw.tick_sound_1);
        mediaPlayer2 = MediaPlayer.create(getContext(), R.raw.tick_sound_2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panicmode, container, false);

        countdownTextView = view.findViewById(R.id.countdown);
        continueButton = view.findViewById(R.id.panicContinueIV);

        timer();
        continueButton.setOnClickListener(v -> {
            countDownTimer.cancel();
            timer();
        });

        Button cancel = view.findViewById(R.id.cancelBT);
        cancel.setOnClickListener(v -> onDestroy());

        return view;
    }

    private void timer(){
        countDownTimer = new CountDownTimer(10100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining time
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
                if(millisUntilFinished/1000%2 == 1)
                    mediaPlayer1.start();
                else
                    mediaPlayer2.start();
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
                mediaPlayer2.release();
                mediaPlayer1.release();
            }
        }.start();
    }


    private boolean isRed = true;
    private void countFinished(){
        countdownTextView.setText(R.string.your_emergency_contacts_have_been_alerted);
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
        mediaPlayer1.stop();
        mediaPlayer2.stop();
        mediaPlayer1.release();
        mediaPlayer2.release();
        countDownTimer.cancel();
        // get fragment manager and remove the fragment
        requireActivity().getSupportFragmentManager().beginTransaction().remove(PanicmodeFragment.this).commit();
    }
}