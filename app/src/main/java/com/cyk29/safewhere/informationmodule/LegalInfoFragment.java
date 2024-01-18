package com.cyk29.safewhere.informationmodule;
//imports
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.InfoItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * LegalInfoFragment is used for displaying legal information.
 * It fetches data from Firebase and displays it using a RecyclerView.
 */
public class LegalInfoFragment extends Fragment {
    private static final String TAG = "LegalInfoFragment";

    private RecyclerView recyclerView;
    private InfoAdapter adapter;
    private List<InfoItem> legalInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_info, container, false);
        initializeUI(view);
        fetchDataFromFirebase();
        setUI();
        return view;
    }

    /**
     * Initializes the UI components of the fragment.
     * @param view The view inflated for the fragment.
     */
    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new InfoAdapter(legalInfoList);
    }

    /**
     * Sets up the UI components such as the RecyclerView.
     */
    private void setUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Fetches data from Firebase and updates the RecyclerView adapter.
     */
    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("legalInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                legalInfoList.clear();
                legalInfoList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    InfoItem infoItem = snapshot.getValue(InfoItem.class);
                    legalInfoList.add(infoItem);
                }
                adapter.updateData(legalInfoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
