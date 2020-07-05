package com.gmail.fuskerr63.android.library.birthday;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.library.R;

public class IntentManager {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";
    private final String ACTION = "com.gmail.fuskerr63.action.notification";

    private final Context context;

    public IntentManager(Context context) {
        this.context = context;
    }

    public Intent getIntent(int id, String text) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_TEXT, text);
        return intent;
    }

    public Intent getIntent(Context context, Class<MainActivity> mainActivityClass, int id) {
        Intent intent = new Intent(context, mainActivityClass);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    public Notification getNotification(int id, String text, int flag, String channeId, int priority) {
        PendingIntent pendingIntent = getPendingIntent(id, text, flag);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channeId)
                .setSmallIcon(R.drawable.android_icon)
                .setContentText(text)
                .setContentTitle(context.getString(R.string.notification_title))
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return notification.build();
    }

    public PendingIntent getPendingIntent(int id, String text, int flag) {
        return PendingIntent.getBroadcast(context, id, getIntent(id, text), flag);
    }
}
