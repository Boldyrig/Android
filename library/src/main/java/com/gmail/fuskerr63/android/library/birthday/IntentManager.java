package com.gmail.fuskerr63.android.library.birthday;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.library.R;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class IntentManager {
    private static final String EXTRA_ID = "ID";
    private static final String EXTRA_NAME = "NAME";
    private static final String EXTRA_TEXT = "TEXT";
    private static final String ACTION = "com.gmail.fuskerr63.action.notification";

    private final transient Context context;
    private final transient Class<MainActivity> mainActivityClass;

    public IntentManager(@Nullable Class<MainActivity> mainActivityClass, @Nullable Context context) {
        this.mainActivityClass = mainActivityClass;
        this.context = context;
    }

    @NonNull
    public Intent getIntent(int id, @Nullable String name, @Nullable String text) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_ID, id);
        if (name != null) {
            intent.putExtra(EXTRA_NAME, name);
        }
        intent.putExtra(EXTRA_TEXT, text);
        return intent;
    }

    @NonNull
    private Intent getIntent(@NonNull Context context, int id) {
        Intent intent = new Intent(context, mainActivityClass);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Nullable
    public Notification getNotification(
            int id,
            @Nullable String text,
            int flag,
            @NonNull String channeId,
            int priority) {
        PendingIntent pendingIntent = getPendingIntent(id, getIntent(context, id), flag);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channeId)
                .setSmallIcon(R.mipmap.android_icon)
                .setContentText(text)
                .setContentTitle(context.getString(R.string.notification_title))
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return notification.build();
    }

    @Nullable
    public PendingIntent getPendingIntent(int id, @Nullable Intent intent, int flag) {
        return PendingIntent.getBroadcast(context, id, intent, flag);
    }
}
