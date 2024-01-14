package com.cyk29.safewhere.informationmodule;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * FAQFragment is responsible for displaying frequently asked questions.
 * It uses Firebase to fetch the data and displays it in a RecyclerView.
 */
public class FAQFragment extends Fragment {
    private static final String TAG = "FAQFragment";

    private RecyclerView recyclerView;
    private InfoAdapter adapter;
    private List<InfoItem> faqInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_info, container, false);
        initializeUI(view);
        fetchDataFromFirebase();
        setUI();
        return view;
    }

    /**
     * Initialize the UI components of the fragment.
     * @param view The view inflated for the fragment.
     */
    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new InfoAdapter(faqInfoList);
    }

    /**
     * Set up the UI components like RecyclerView.
     */
    private void setUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Fetch data from Firebase and update the adapter.
     */
    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("faqInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                faqInfoList.clear();
                faqInfoList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    InfoItem infoItem = snapshot.getValue(InfoItem.class);
                    faqInfoList.add(infoItem);
                }
                adapter.updateData(faqInfoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
