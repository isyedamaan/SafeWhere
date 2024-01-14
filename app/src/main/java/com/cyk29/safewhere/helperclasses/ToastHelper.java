package com.cyk29.safewhere.helperclasses;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import android.content.Context;

import com.cyk29.safewhere.R;

/**
 * Utility class for displaying custom toasts.
 */
public class ToastHelper {

    /**
     * Displays a custom toast with the specified message and duration.
     *
     * @param context The application context.
     * @param message The message to display.
     * @param duration The duration for which the toast should be displayed.
     */
    public static void make(Context context, String message, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}

