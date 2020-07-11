package com.gmail.fuskerr63.android.library.birthday;

import android.app.AlarmManager;
import android.app.NotificationManager;

import com.gmail.fuskerr63.java.entity.BirthdayCalendar;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.reactivex.annotations.Nullable;

public class NotificationRepositoryImpl implements NotificationRepository {
    private transient final AlarmManager alarmManager;
    private transient final NotificationManager notificationManager;
    private transient final IntentManager intentManager;

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
    public void setAlarm(@Nullable BirthdayCalendar birthdayCalendar, int id, @Nullable String text, int flag) {
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
        intentManager.getPendingIntent(id, intentManager.getIntent(id, null, text), flag).cancel();
    }

    @Override
    public void notifyNotification(int id, @Nullable String text, int flag, @Nullable String channelId, int priority) {
        notificationManager.notify(id, intentManager.getNotification(id, text, flag, channelId, priority));
    }

    @SuppressWarnings(UNUSED)
    @Override
    public boolean alarmIsUp(int id, @Nullable String text, int flag) {
        return intentManager.getPendingIntent(id, intentManager.getIntent(id, null, text), flag) != null;
    }
}
