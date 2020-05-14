package com.gmail.fuskerr63.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.androidlesson.MainActivity;
import com.gmail.fuskerr63.androidlesson.R;

public class ContactReceiver extends BroadcastReceiver {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String CHANNEL_ID = "channelId";
        final String CHANNEL_NAME = "channelName";
        final String NOTIFICATION_TITLE = "Contacts";

        Bundle extras = intent.getExtras();
        if(extras != null) {
            String text = extras.getString(EXTRA_TEXT);
            int id = extras.getInt(EXTRA_ID);
            Intent pIntent = new Intent(context, MainActivity.class);
            pIntent.putExtra(EXTRA_ID, id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, pIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.android_icon)
                    .setContentText(text)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(context.getString(R.string.channel_description));
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(id, notification.build());
        }
    }
}
