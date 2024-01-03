package com.cyk29.safewhere.informationmodule;

import android.os.Bundle;
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

public class LegalInfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private InfoAdapter adapter;
    private DatabaseReference databaseReference;
    private List<InfoItem> legalInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_info, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);


        databaseReference = FirebaseDatabase.getInstance().getReference("legalInfo");

        adapter = new InfoAdapter(legalInfoList);

        fetchDataFromFirebase();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void fetchDataFromFirebase() {
        // Attach a listener to read the data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                legalInfoList.clear();
                legalInfoList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convert the data from Firebase to InfoItem objects
                    InfoItem infoItem = snapshot.getValue(InfoItem.class);
                    legalInfoList.add(infoItem);
                }
                // Update the adapter with the retrieved data
                adapter.updateData(legalInfoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
