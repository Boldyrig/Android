package com.gmail.fuskerr63.android.library.birthday;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.gmail.fuskerr63.java.entity.BirthdayCalendar;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class NotificationRepositoryImpl implements NotificationRepository {
    private final transient AlarmManager alarmManager;
    private final transient NotificationManager notificationManager;
    private final transient IntentManager intentManager;

    private static final String UNUSED = "unused";

    @SuppressWarnings(UNUSED)
    public NotificationRepositoryImpl(
            @Nullable AlarmManager alarmManager,
            @Nullable NotificationManager notificationManager,
            @Nullable IntentManager intentManager) {
        this.alarmManager = alarmManager;
        this.notificationManager = notificationManager;
        this.intentManager = intentManager;
    }

    @SuppressWarnings(UNUSED)
    @Override
    public void setAlarm(@NonNull BirthdayCalendar birthdayCalendar, int id, @Nullable String text, int flag) {
        Calendar birthday = new GregorianCalendar(
                birthdayCalendar.getYear(),
                birthdayCalendar.getMonth(),
                birthdayCalendar.getDay(),
                birthdayCalendar.getHour(),
                birthdayCalendar.getMinute(),
                birthdayCalendar.getSecond()
        );
        alarmManager.set(
                AlarmManager.RTC,
                birthday.getTimeInMillis(),
                intentManager.getPendingIntent(
                        id,
                        intentManager.getIntent(id, null, text),
                        flag
                ));
    }

    @SuppressWarnings(UNUSED)
    @Override
    public void cancelAlarm(int id, @Nullable String text, int flag) {
        alarmManager.cancel(
                intentManager.getPendingIntent(
                        id,
                        intentManager.getIntent(id, null, text),
                        flag
                ));
        PendingIntent pendingIntent = intentManager.getPendingIntent(id, intentManager.getIntent(id, null, text), flag);
        if (pendingIntent != null) {
            pendingIntent.cancel();
        }
    }

    @Override
    public void notifyNotification(int id, @Nullable String text, int flag, @NonNull String channelId, int priority) {
        notificationManager.notify(id, intentManager.getNotification(id, text, flag, channelId, priority));
    }

    @SuppressWarnings(UNUSED)
    @Override
    public boolean alarmIsUp(int id, @Nullable String text, int flag) {
        return intentManager.getPendingIntent(id, intentManager.getIntent(id, null, text), flag) != null;
    }
}
