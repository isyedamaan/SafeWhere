package com.cyk29.safewhere.startupmodule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.databinding.FragmentSignUpBinding;
import com.google.android.material.tabs.TabLayout;

public class SignUpLoginFragment extends Fragment{

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstancedState){
        FragmentSignUpBinding binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Sets up the back button.
     *
     * @param view The view associated with this fragment.
     */
    private void setupBackButton(View view) {
        ImageButton BtnBack = view.findViewById(R.id.signUp_back_IB);
        BtnBack.setOnClickListener(v -> navigateToIntroScreen());
    }

    /**
     * Navigates to the intro screen.
     */
    private void navigateToIntroScreen() {
        Navigation.findNavController(requireView()).navigate(R.id.DestIntro);
    }

    /**
     * Sets up the login/sign up tabs.
     *
     * @param view The view associated with this fragment.
     */
    private void setupTabs(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));

        setupViewPagerAndTabLayout();
    }

    /**
     * Sets up the view pager and tab layout.
     */
    private void setupViewPagerAndTabLayout() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ViewPageAdapter adapter = new ViewPageAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBackButton(view);
        setupTabs(view);
    }

}