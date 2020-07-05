package com.gmail.fuskerr63.android.library.birthday;

import android.app.AlarmManager;
import android.app.NotificationManager;

import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationRepositoryImpl implements NotificationRepository {
    private final AlarmManager alarmManager;
    private final NotificationManager notificationManager;
    private final IntentManager intentManager;

    public NotificationRepositoryImpl(AlarmManager alarmManager, NotificationManager notificationManager, IntentManager intentManager) {
        this.alarmManager = alarmManager;
        this.notificationManager = notificationManager;
        this.intentManager = intentManager;
    }

    @Override
    public void setAlarm(int year, int month, int day, int hour, int minute, int second, int id, String text, int flag) {
        Calendar birthday = new GregorianCalendar(year, month, day, hour, minute, second);
        alarmManager.set(alarmManager.RTC, birthday.getTimeInMillis(), intentManager.getPendingIntent(id, text, flag));
    }

    @Override
    public void cancelAlarm(int id, String text, int flag) {
        alarmManager.cancel(intentManager.getPendingIntent(id, text, flag));
        intentManager.getPendingIntent(id, text, flag).cancel();
    }

    @Override
    public void notifyNotification(int id, String text, int flag, String channelId, int priority) {
        notificationManager.notify(id, intentManager.getNotification(id, text, flag, channelId, priority));
    }

    @Override
    public boolean alarmIsUp(int id, String text, int flag) {
        return intentManager.getPendingIntent(id, text, flag) != null;
    }
}
