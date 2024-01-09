package com.cyk29.safewhere.mapmodule;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.Report;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HotSpotFragment extends BottomSheetDialogFragment {
    private final Report report;
    public HotSpotFragment(Report report) {
        this.report = report;
    }
    public interface BottomSheetListener {
        void showHotspots();
    }
    private BottomSheetListener mListener;
    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    private TextView dangerDescription, upVotes, userDescription, downVotes;
    private boolean upVoted = false, downVoted = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_stop, container, false);
        initializeUI(view);
        setDangerDescriptionText();
        setUserDescriptionText();
        return view;
    }

    private void setUserDescriptionText() {
        String part1 = "User description:\n";
        String description = report.getUserDescription();

        SpannableString spannableString = new SpannableString(part1 + description);

        ForegroundColorSpan blueSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black));
        spannableString.setSpan(blueSpan, 0, part1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        userDescription.setText(spannableString);
    }

    private void setDangerDescriptionText() {
        String part1 = "This area was reported for ";
        String type = report.getType();
        String part2 = " by user ";
        String name = report.getUserName();

        SpannableString spannableString = new SpannableString(part1 + type + part2 + name+".");

        ForegroundColorSpan blueSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.blue));
        spannableString.setSpan(blueSpan, 0, part1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan redSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red));
        spannableString.setSpan(redSpan, part1.length(), part1.length() + type.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan maroonSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.maroon));
        spannableString.setSpan(maroonSpan, part1.length() + type.length() + part2.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        dangerDescription.setText(spannableString);
    }

    private void initializeUI(View view) {
        dangerDescription = view.findViewById(R.id.hotspot_danger_description);
        userDescription = view.findViewById(R.id.hotspot_user_description);
        upVotes = view.findViewById(R.id.hotspot_upvotes);
        downVotes = view.findViewById(R.id.hotspot_downvotes);
        ImageView upVoteButton = view.findViewById(R.id.hotspot_upvoteIV);
        upVotes.setText(String.valueOf(report.getUpVotes()));
        upVoteButton.setOnClickListener(v -> {
            int up = Integer.parseInt(upVotes.getText().toString());
            if(!upVoted) {
                upVotes.setText(String.valueOf(up + 1));
                upVoted = true;
            } else {
                upVotes.setText(String.valueOf(up - 1));
                upVoted = false;
            }
        });
        downVotes.setText(String.valueOf(report.getDownVotes()));
        ImageView downVoteButton = view.findViewById(R.id.hotspot_downvoteIV);
        downVoteButton.setOnClickListener(v -> {
            int down = Integer.parseInt(downVotes.getText().toString());
            if(!downVoted) {
                downVotes.setText(String.valueOf(down - 1));
                downVoted = true;
            } else {
                downVotes.setText(String.valueOf(down + 1));
                downVoted = false;
            }
        });
        ImageView topLine = view.findViewById(R.id.hotspot_topline);
        topLine.setOnClickListener(v -> dismiss());
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports").child(report.getId());
        ref.child("upVotes").setValue(Integer.parseInt(upVotes.getText().toString()));
        ref.child("downVotes").setValue(Integer.parseInt(downVotes.getText().toString()));
        if (mListener != null)
            mListener.showHotspots();

    }
}