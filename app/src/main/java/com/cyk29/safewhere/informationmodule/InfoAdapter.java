package com.cyk29.safewhere.informationmodule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.ChangeBounds;
import android.transition.TransitionManager;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.dataclasses.InfoItem;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<InfoItem> infoItemList;

    public InfoAdapter(List<InfoItem> infoItemList) {
        this.infoItemList = infoItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoItem infoItem = infoItemList.get(position);

        holder.textTitle.setText(infoItem.getTitle());
        holder.textDescription.setText(infoItem.getDescription());
        if(infoItem.getLink()==null) {
            holder.textLink.setVisibility(View.GONE);
        } else {
            holder.textLink.setText("For more info: " + infoItem.getLink());
        }

        holder.layoutDetails.setVisibility(View.GONE);

        // Set click listener to toggle visibility of details
        holder.itemView.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) holder.itemView.getParent();
            if (holder.layoutDetails.getVisibility() == View.GONE) {
                // Expand details with animation
                TransitionManager.beginDelayedTransition(parent, new ChangeBounds());
                holder.layoutDetails.setVisibility(View.VISIBLE);
            } else {
                // Collapse details with animation
                TransitionManager.beginDelayedTransition(parent, new ChangeBounds());
                holder.layoutDetails.setVisibility(View.GONE);
            }
        });

        holder.textLink.setOnClickListener(v -> {
            // Open the link in the browser
            openLinkInBrowser(holder.itemView.getContext(), infoItem.getLink());
        });
    }

    private void openLinkInBrowser(Context context, String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);
    }

    public void updateData(List<InfoItem> newData) {
        infoItemList.clear();
        infoItemList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return infoItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription, textLink;
        LinearLayout layoutDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLink = itemView.findViewById(R.id.textLink);
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
        }
    }
}

