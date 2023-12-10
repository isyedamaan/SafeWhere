package com.cyk29.safewhere.startup;

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
import android.widget.Toast;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.databinding.FragmentSignUpBinding;
import com.google.android.material.tabs.TabLayout;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPageAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        //Button
        ImageButton BtnSignup = view.findViewById(R.id.SignUpBT);
        View.OnClickListener OCLSignup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TO BE CONTINUED", Toast.LENGTH_SHORT).show();
            }
        };
        BtnSignup.setOnClickListener(OCLSignup);

        //BACK BUTTON
        ImageButton BtnBack = view.findViewById(R.id.backBT);
        View.OnClickListener OCLback = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestIntro);
            }
        };
        BtnBack.setOnClickListener(OCLback);

        //LOGIN?SIGNUP TAB
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapter = new ViewPageAdapter(fragmentManager, getLifecycle());
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstancedState){

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

}