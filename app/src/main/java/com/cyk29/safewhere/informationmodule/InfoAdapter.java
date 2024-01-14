package com.cyk29.safewhere.informationmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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

/**
 * InfoAdapter is a RecyclerView adapter for displaying a list of InfoItem objects.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private static final String TAG = "InfoAdapter";
    private final List<InfoItem> infoItemList;

    public InfoAdapter(List<InfoItem> infoItemList) {
        this.infoItemList = infoItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoItem infoItem = infoItemList.get(position);
        holder.bind(infoItem);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<InfoItem> newData) {
        Log.d(TAG, "updateData: "+newData.size()+" items");
        infoItemList.clear();
        infoItemList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return infoItemList.size();
    }

    /**
     * ViewHolder for the InfoItem.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription, textLink;
        LinearLayout layoutDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLink = itemView.findViewById(R.id.textLink);
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
            layoutDetails.setVisibility(View.GONE);
        }

        /**
         * Binds the InfoItem to the ViewHolder.
         * @param infoItem The InfoItem to bind.
         */
        void bind(InfoItem infoItem) {
            textTitle.setText(infoItem.getTitle());
            textDescription.setText(infoItem.getDescription());
            setupLink(itemView.getContext(), infoItem.getLink());
            setupItemViewClickListener();
        }

        /**
         * Sets up the click listener for the item view.
         */
        private void setupItemViewClickListener() {
            itemView.setOnClickListener(v -> toggleDetailsVisibility());
        }

        /**
         * Toggles the visibility of layoutDetails with animation.
         */
        private void toggleDetailsVisibility() {
            ViewGroup parent = (ViewGroup) itemView.getParent();
            TransitionManager.beginDelayedTransition(parent, new ChangeBounds());
            layoutDetails.setVisibility(layoutDetails.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }

        /**
         * Sets up the text and click listener for the link.
         * @param context The context of the itemView.
         * @param link The URL to be opened.
         */
        private void setupLink(Context context, String link) {
            if (link == null) {
                textLink.setVisibility(View.GONE);
            } else {
                textLink.setText(context.getString(R.string.for_more_info, link));
                textLink.setOnClickListener(v -> openLinkInBrowser(context, link));
            }
        }

        /**
         * Opens the provided link in a web browser.
         * @param context The context used to start the activity.
         * @param link The URL to be opened.
         */
        private void openLinkInBrowser(Context context, String link) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(browserIntent);
        }
    }
}
