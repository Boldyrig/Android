package com.gmail.fuskerr63.android.library.birthday;

import android.app.PendingIntent;

import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import java.util.Calendar;

public class BirthdayNotification {
    private final NotificationInteractor notificationInteractor;

    private final String CHANNEL_ID = "channelId";

    private final int PRIORITY_DEFAULT = NotificationCompat.PRIORITY_DEFAULT;
    private final int FLAG_NO_CREATE = PendingIntent.FLAG_NO_CREATE;
    private final int FLAG_UPDATE_CURRENT = PendingIntent.FLAG_UPDATE_CURRENT;

    public BirthdayNotification(NotificationInteractor notificationInteractor) {
        this.notificationInteractor = notificationInteractor;
    }

    public void changeAlarmStatus(int id, String text, Calendar birthday) {
        notificationInteractor.changeAlarmStatus(id, text, birthday, FLAG_UPDATE_CURRENT, FLAG_NO_CREATE);
    }

    public String getButtonText(int id, String text, String send, String cancel) {
        return notificationInteractor.getButtonText(id, text, send, cancel, FLAG_NO_CREATE);
    }

    public void setBirthdayAlarm(int id, Calendar birthday, String text) {
        Calendar nextBirthday = notificationInteractor.getNextBirthday(birthday);
        //PendingIntent pendingIntent = getPendingIntent(context, id, intentManager.getIntent(id, text), FLAG_UPDATE_CURRENT);
        notificationInteractor.setAlarm(nextBirthday, id, text, FLAG_UPDATE_CURRENT);
    }

    public void notifyNorification(int id, String text) {
        notificationInteractor.notifyNotification(id, text, FLAG_UPDATE_CURRENT, CHANNEL_ID, PRIORITY_DEFAULT);
    }
}
