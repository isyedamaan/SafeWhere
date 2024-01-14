package com.cyk29.safewhere.mapmodule;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.Report;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A fragment to display hotspot information.
 */
public class HotSpotFragment extends BottomSheetDialogFragment {
    private final Report report;
    private TextView dangerDescription, upVotes, userDescription, downVotes;
    private boolean upVoted = false, downVoted = false;

    /**
     * Constructs a new HotSpotFragment.
     *
     * @param report The report associated with the hotspot.
     */
    public HotSpotFragment(Report report) {
        this.report = report;
    }

    /**
     * Interface to handle interactions with the bottom sheet.
     */
    public interface BottomSheetListener {
        /**
         * Show hotspots.
         */
        void showHotspots();
    }

    private BottomSheetListener mListener;

    /**
     * Set the BottomSheetListener for this fragment.
     *
     * @param listener The BottomSheetListener to set.
     */
    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_stop, container, false);
        initializeUI(view);
        setDangerDescriptionText();
        setUserDescriptionText();
        return view;
    }

    /**
     * Set the user description text with proper formatting.
     */
    private void setUserDescriptionText() {
        String part1 = "User description:\n";
        String description = report.getUserDescription();
        SpannableString spannableString = new SpannableString(part1 + description);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, part1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        userDescription.setText(spannableString);
    }

    /**
     * Set the danger description text with proper formatting.
     */
    private void setDangerDescriptionText() {
        String part1 = "This area was reported for ";
        String type = report.getType();
        String part2 = " by user ";
        String name = report.getUserName();

        SpannableString spannableString = new SpannableString(part1 + type + part2 + name + ".");
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.blue)), 0, part1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)), part1.length(), part1.length() + type.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.maroon)), part1.length() + type.length() + part2.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        dangerDescription.setText(spannableString);
    }

    /**
     * Initialize UI elements and set up click listeners.
     *
     * @param view The view of the fragment.
     */
    private void initializeUI(View view) {
        dangerDescription = view.findViewById(R.id.hotspot_danger_description);
        userDescription = view.findViewById(R.id.hotspot_user_description);
        upVotes = view.findViewById(R.id.hotspot_upvotes);
        downVotes = view.findViewById(R.id.hotspot_downvotes);

        ImageView upVoteButton = view.findViewById(R.id.hotspot_upvoteIV);
        upVotes.setText(String.valueOf(report.getUpVotes()));
        upVoteButton.setOnClickListener(v -> updateVoteCount(upVotes, !upVoted));

        ImageView downVoteButton = view.findViewById(R.id.hotspot_downvoteIV);
        downVotes.setText(String.valueOf(report.getDownVotes()));
        downVoteButton.setOnClickListener(v -> updateVoteCount(downVotes, !downVoted));

        ImageView topLine = view.findViewById(R.id.hotspot_topline);
        topLine.setOnClickListener(v -> dismiss());
    }

    /**
     * Update the vote count and toggle the vote status.
     *
     * @param textView  The TextView to update.
     * @param isUpvote  True if it's an upvote, false if it's a downvote.
     */
    private void updateVoteCount(TextView textView, boolean isUpvote) {
        int count = Integer.parseInt(textView.getText().toString());
        if (isUpvote) {
            textView.setText(String.valueOf(count + 1));
        } else {
            textView.setText(String.valueOf(count - 1));
        }

        if (isUpvote) {
            upVoted = !upVoted;
        } else {
            downVoted = !downVoted;
        }
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
