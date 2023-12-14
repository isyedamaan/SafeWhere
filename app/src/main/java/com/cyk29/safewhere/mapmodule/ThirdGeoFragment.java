package com.cyk29.safewhere.mapmodule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyk29.safewhere.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdGeoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdGeoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThirdGeoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdGeoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdGeoFragment newInstance(String param1, String param2) {
        ThirdGeoFragment fragment = new ThirdGeoFragment();
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

    Button finish;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_geo, container, false);

        finish = view.findViewById(R.id.finishGeoBT);
        finish.setOnClickListener(v -> {
            // Navigate to Fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FCVFirst, new FinalGeoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}