package com.gmail.fuskerr63.android.library.birthday;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public interface NotificationRepository {
    void setAlarmManager(Calendar birthday, PendingIntent pendingIntent);
    void cancelAlarmManager(PendingIntent pendingIntent);

    void notifyNotification(int id, Notification notification);

    boolean alarmIsUp(Context context, int id, Intent intent, int flag);
}
