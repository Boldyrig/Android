package com.gmail.fuskerr63.android.library.birthday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class NotificationRepositoryImpl implements NotificationRepository {
    private final AlarmManager alarmManager;
    private final NotificationManager notificationManager;

    public NotificationRepositoryImpl(AlarmManager alarmManager, NotificationManager notificationManager) {
        this.alarmManager = alarmManager;
        this.notificationManager = notificationManager;
    }

    @Override
    public void setAlarmManager(Calendar birthday, PendingIntent pendingIntent) {
        alarmManager.set(alarmManager.RTC, birthday.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void cancelAlarmManager(PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    @Override
    public void notifyNotification(int id, Notification notification) {
        notificationManager.notify(id, notification);
    }

    @Override
    public boolean alarmIsUp(Context context, int id, Intent intent, int flag) {
        return PendingIntent.getBroadcast(context, id, intent, flag) != null;
    }
}
