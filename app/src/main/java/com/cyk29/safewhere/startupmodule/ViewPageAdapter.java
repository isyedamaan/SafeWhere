package com.cyk29.safewhere.startupmodule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAdapter extends FragmentStateAdapter{

    /**
     * Constructs a new instance of {@link ViewPageAdapter}.
     *
     * @param fragmentManager The fragment manager.
     * @param lifecycle The lifecycle.
     */
    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }

    /**
     * Creates a new fragment based on the given position.
     *
     * @param position The position of the fragment.
     * @return The created fragment.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position == 1){
            return new SignUpTabFragment();
        }
        return new LoginTabFragment();
    }

    /**
     * Gets the count of items.
     *
     * @return The item count.
     */
    @Override public int getItemCount(){
        return 2;
    }
}
