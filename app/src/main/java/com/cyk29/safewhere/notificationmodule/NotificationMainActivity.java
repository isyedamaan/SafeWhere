package com.cyk29.safewhere.notificationmodule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.NotificationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List; // Import this if you're using a list
import java.util.Locale;
import java.util.Objects;

public class NotificationMainActivity extends AppCompatActivity {

    private RecyclerView notificationRV;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main);

        notificationRV = findViewById(R.id.notificationRV);
        notificationRV.setLayoutManager(new LinearLayoutManager(this));

        notificationItems = new ArrayList<>(); // Initialize the list
        adapter = new NotificationAdapter(notificationItems); // Set the adapter with an empty list
        notificationRV.setAdapter(adapter);

        loadNotificationItems("All"); // Load the notification items



        Spinner filterSpinner = findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notification_filter_options, R.layout.spinner_item_fixed_text);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selection
                String selectedFilter = parent.getItemAtPosition(position).toString();
                switch (selectedFilter) {
                    case "All":
                        loadNotificationItems("All");
                        break;
                    case "Danger":
                        loadNotificationItems("dangerzone");
                        break;
                    case "Report":
                        loadNotificationItems("report");
                        break;
                    case "Geofencing":
                        loadNotificationItems("geofencing");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        ImageView backNotifBT = findViewById(R.id.backNotifBT);
        backNotifBT.setOnClickListener(v -> finish()); // Close activity on back button click
    }

    private void loadNotificationItems(String selectedType) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("notifications");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationItems.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.child("id").getValue(String.class);
                    String message = snapshot.child("message").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);
                    if (id != null && message != null && type != null && time != null) {
                        NotificationItem item = new NotificationItem(id, message, time,type);
                        if (selectedType.equals(type)) {
                            notificationItems.add(item);
                        }
                        if(selectedType.equals("All")){
                            notificationItems.add(item);
                        }
                    }
                }
                sort(notificationItems);
                adapter.notifyDataSetChanged(); // Notify the adapter of the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log or handle database errors
            }
        });
    }


    List<NotificationItem> sort(List<NotificationItem> notificationItems){

        Collections.sort(notificationItems, new Comparator<NotificationItem>() {
            @Override
            public int compare(NotificationItem o1, NotificationItem o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        return notificationItems;
    }


    private static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

        private final List<NotificationItem> notificationItems;

        NotificationAdapter(List<NotificationItem> notificationItems) {
            this.notificationItems = notificationItems;
        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            NotificationItem item = notificationItems.get(position);
            holder.notificationMessageTV.setText(item.getMessage());

            holder.notificationTimeTV.setText(formatElapsedTime(item.getTime()));
            holder.bindImage(item.getType()); // Update the ImageView based on the notification type
        }

        private String formatElapsedTime(String notificationTime) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            long currentTime = System.currentTimeMillis();
            Date date = new Date(currentTime);
            Log.d("NotificationMainActivity", "formatElapsedTime: " + date);
            try {
                Date notificationDate = dateFormat.parse(notificationTime);
                long notificationTimestamp = notificationDate.getTime();
                long elapsedTime = currentTime - notificationTimestamp; // Time difference in milliseconds

                if (elapsedTime < 60000) { // Less than 1 minute
                    return "now";
                } else if (elapsedTime < 3600000) { // Less than 1 hour
                    return (elapsedTime / 60000) + "m";
                } else if (elapsedTime < 86400000) { // Less than 1 day
                    return (elapsedTime / 3600000) + "hr";
                } else if (elapsedTime < 604800000) { // Less than 1 week
                    return (elapsedTime / 86400000) + "d";
                } else {
                    // You can continue for weeks, months, years as you prefer
                    return (elapsedTime / 604800000) + "w";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }



        @Override
        public int getItemCount() {
            return notificationItems.size();
        }

        static class NotificationViewHolder extends RecyclerView.ViewHolder {

            ImageView notificationIV;
            TextView notificationMessageTV;
            TextView notificationTimeTV;
            ConstraintLayout notificationCL;

            NotificationViewHolder(View view) {
                super(view);
                notificationIV = view.findViewById(R.id.notificationIV);
                notificationMessageTV = view.findViewById(R.id.notificationMessageTV);
                notificationTimeTV = view.findViewById(R.id.notificationTimeTV);
                notificationCL = view.findViewById(R.id.notifLayout);
            }


            void bindImage(String type) {
                switch (type) {
                    case "dangerzone":
                        notificationIV.setImageResource(R.drawable.dangerzone_icon);
                        notificationCL.setBackgroundColor(notificationCL.getResources().getColor(R.color.maroon));
                        notificationMessageTV.setTextColor(notificationMessageTV.getResources().getColor(R.color.white));
                        break;
                    case "report":
                        notificationIV.setImageResource(R.drawable.report_icon3);
                        notificationCL.setBackgroundColor(notificationCL.getResources().getColor(R.color.pink));
                        notificationMessageTV.setTextColor(notificationMessageTV.getResources().getColor(R.color.black));
                        setImageViewWidth(notificationIV, 50);
                        break;
                    case "geofencing":
                        notificationIV.setImageResource(R.drawable.geofencing_icon);
                        notificationCL.setBackgroundColor(notificationCL.getResources().getColor(R.color.coral));
                        notificationMessageTV.setTextColor(notificationMessageTV.getResources().getColor(R.color.white));
                        setImageViewWidth(notificationIV, 50);
                        break;
                    default:
                        break;
                }
            }

            private void setImageViewWidth(ImageView imageView, int widthInDp) {
                float scale = imageView.getContext().getResources().getDisplayMetrics().density;
                int widthInPx = (int) (widthInDp * scale + 0.5f);
                imageView.getLayoutParams().width = widthInPx;
                imageView.requestLayout(); // This is important to apply the new height
            }
        }

    }

}
