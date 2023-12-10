package com.cyk29.safewhere.startup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.databinding.FragmentIntroBinding;


public class IntroFragment extends Fragment {


    private FragmentIntroBinding binding;

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        //Button
        ImageButton BtnIntro = view.findViewById(R.id.introBT);
        View.OnClickListener OCLIntro = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestSignup);
            }
        };
        BtnIntro.setOnClickListener(OCLIntro);
    }

    @Override
    //IMPORTANT TO BIND FOR IT TO APPEAR ON SCREEN
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentIntroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;


    }
}