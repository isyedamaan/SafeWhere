package com.cyk29.safewhere.startupmodule;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyk29.safewhere.R;

public class LoginTabFragment extends Fragment {

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //FORGOT PASSWORD
        TextView textView = view.findViewById(R.id.ForgotPassTV);

        String text = "Forgot password?";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getActivity(), "bruh what", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 0,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        //Remember me button
        RadioButton radioButton = view.findViewById(R.id.radioBT);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioButton.isSelected()) {
                    radioButton.setChecked(true);
                    radioButton.setSelected(true);
                } else {
                    radioButton.setChecked(false);
                    radioButton.setSelected(false);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }
}