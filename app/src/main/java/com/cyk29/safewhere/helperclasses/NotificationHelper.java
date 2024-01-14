package com.cyk29.safewhere.helperclasses;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cyk29.safewhere.R;

import java.util.Random;

/**
 * Helper class for managing notifications.
 */
public class NotificationHelper extends ContextWrapper {
    private static final String TAG = "NotificationHelper";

    /**
     * Constructs a new instance of {@link NotificationHelper}.
     *
     * @param base The base context.
     */
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    private final String CHANNEL_NAME = "High priority channel";
    private final String CHANNEL_ID = "com.cyk29.safewhere" + CHANNEL_NAME;

    /**
     * Creates notification channels for Android O and above.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("high priority notification channel for SafeWhere");
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }

    /**
     * Sends a high priority notification.
     *
     * @param title The title of the notification.
     * @param body The body of the notification.
     * @param summary The summary of the notification.
     * @param activityName The activity to open when the notification is clicked.
     */
    public void sendHighPriorityNotification(String title, String body, String summary, Class<? extends Activity> activityName) {
        Intent intent = new Intent(this, activityName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSubText(summary)
                .setSmallIcon(R.drawable.app_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().setSummaryText("summary").setBigContentTitle(title).bigText(body))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ToastHelper.make(this, "Notification permission not granted", Toast.LENGTH_SHORT);
            return;
        }
        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);
        Log.d(TAG, "sendHighPriorityNotification: sent");
    }
}












