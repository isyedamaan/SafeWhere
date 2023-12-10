package com.cyk29.safewhere.startup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAdapter extends FragmentStateAdapter{

    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position == 1){
            return new SignUpTabFragment();
        }
        return new LoginTabFragment();
    }

    @Override public int getItemCount(){
        return 2;
    }
}
