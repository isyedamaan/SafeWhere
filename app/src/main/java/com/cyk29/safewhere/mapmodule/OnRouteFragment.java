package com.cyk29.safewhere.mapmodule;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.startupmodule.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnRouteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnRouteFragment newInstance(String param1, String param2) {
        OnRouteFragment fragment = new OnRouteFragment();
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


    Button endBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_route, container, false);

        endBtn = view.findViewById(R.id.endBtn);
        endBtn.setOnClickListener(v -> {
            if (getActivity() instanceof MapsActivity) {
                ((MapsActivity) getActivity()).clearBackStack();
            }
            // Navigate to Fragment2
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVFirst, new FirstFragment())
                    .addToBackStack(null)
                    .commit();
        });



        return view;
    }
}