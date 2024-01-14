package com.cyk29.safewhere.startupmodule;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.databinding.FragmentIntroBinding;

/**
 * This fragment represents the introductory screen of the SafeWhere application's startup module.
 * It provides a button for users to start the app and displays animations and transitions during
 * the startup process.
 */
public class IntroFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeIntroButton(view);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.cyk29.safewhere.databinding.FragmentIntroBinding binding = FragmentIntroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Initializes the introductory button's click listener to trigger animations and transitions.
     *
     * @param view The fragment's root view.
     */
    private void initializeIntroButton(View view) {
        ImageButton introButton = view.findViewById(R.id.startup_next_Btn);
        introButton.setOnClickListener(v -> startStartupAnimations(view));
    }

    /**
     * Starts the startup animations and transitions when the introductory button is clicked.
     *
     * @param view The fragment's root view.
     */
    private void startStartupAnimations(View view) {
        ImageView topRight = view.findViewById(R.id.startup_top_right);
        ImageView bottomLeft = view.findViewById(R.id.startup_bottom_left);
        ConstraintLayout startupLayout = view.findViewById(R.id.startup_intro_cl);
        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.top_right);
        topRight.startAnimation(animation1);
        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_left);
        bottomLeft.startAnimation(animation2);
        Animation animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.startup_fade_out);
        startupLayout.startAnimation(animation3);

        ConstraintLayout backgroundLayout = view.findViewById(R.id.startup_back_cl);
        new android.os.Handler().postDelayed(
                () -> setBackgroundTransition(backgroundLayout),
                350);

        new android.os.Handler().postDelayed(
                this::navigateToSignup,
                700);
    }

    /**
     * Applies a background transition from pink to blue for the specified layout.
     *
     * @param backgroundLayout The layout to apply the transition to.
     */
    private void setBackgroundTransition(ConstraintLayout backgroundLayout) {
        TransitionDrawable transition = new TransitionDrawable(new android.graphics.drawable.Drawable[]{
                ContextCompat.getDrawable(requireContext(), R.color.pink),
                ContextCompat.getDrawable(requireContext(), R.color.blue)
        });
        backgroundLayout.setBackground(transition);
        transition.startTransition(350);
    }

    /**
     * Navigates to the signup destination.
     */
    private void navigateToSignup() {
        Navigation.findNavController(requireView()).navigate(R.id.DestSignup);
    }
}
