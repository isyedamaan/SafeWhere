package com.cyk29.safewhere.sosmodule;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyk29.safewhere.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PanicmodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PanicmodeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PanicmodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PanicmodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PanicmodeFragment newInstance(String param1, String param2) {
        PanicmodeFragment fragment = new PanicmodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private TextView countdownTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panicmode, container, false);

        countdownTextView = view.findViewById(R.id.countdown);

        new CountDownTimer(11000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining time
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Countdown finished, you can perform any action here
                countFinished();
            }
        }.start();

        return view;
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