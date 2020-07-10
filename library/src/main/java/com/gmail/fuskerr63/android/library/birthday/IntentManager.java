package com.gmail.fuskerr63.android.library.birthday;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.library.R;

public class IntentManager {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_NAME = "NAME";
    private final String EXTRA_TEXT = "TEXT";
    private final String ACTION = "com.gmail.fuskerr63.action.notification";

    private Context context;
    private Class<MainActivity> mainActivityClass;

    public IntentManager(Class<MainActivity> mainActivityClass, Context context) {
        this.mainActivityClass = mainActivityClass;
        this.context = context;
    }

    public Intent getIntent(int id, @Nullable String name, String text) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_ID, id);
        if(name != null) {
            intent.putExtra(EXTRA_NAME, name);
        }
        intent.putExtra(EXTRA_TEXT, text);
        return intent;
    }

    public Intent getIntent(Context context, int id) {
        Intent intent = new Intent(context, mainActivityClass);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    public Notification getNotification(int id, String text, int flag, String channeId, int priority) {
        PendingIntent pendingIntent = getPendingIntent(id, getIntent(context, id), flag);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channeId)
                .setSmallIcon(R.drawable.android_icon)
                .setContentText(text)
                .setContentTitle(context.getString(R.string.notification_title))
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return notification.build();
    }

    public PendingIntent getPendingIntent(int id, Intent intent, int flag) {
        return PendingIntent.getBroadcast(context, id, intent, flag);
    }
}
