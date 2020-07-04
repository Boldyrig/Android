package com.gmail.fuskerr63.android.library.birthday;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.library.R;

import java.util.Calendar;

public class BirthdayNotification {
    private final NotificationRepository notificationRepository;
    private final NotificationInteractor notificationInteractor;
    private final IntentManager intentManager;

    private final String CHANNEL_ID = "channelId";

    private final int PRIORITY_DEFAULT = NotificationCompat.PRIORITY_DEFAULT;
    private final int FLAG_NO_CREATE = PendingIntent.FLAG_NO_CREATE;
    private final int FLAG_UPDATE_CURRENT = PendingIntent.FLAG_UPDATE_CURRENT;

    public BirthdayNotification(NotificationRepository notificationRepository, NotificationInteractor notificationInteractor, IntentManager intentManager) {
        this.notificationRepository = notificationRepository;
        this.notificationInteractor = notificationInteractor;
        this.intentManager = intentManager;
    }

    public void setBirthdayAlarm(Context context, int id, Calendar birthday, String text) {
        Calendar nextBirthday = notificationInteractor.getNextBirthday(birthday);
        PendingIntent pendingIntent = getPendingIntent(context, id, intentManager.getIntent(id, text), FLAG_UPDATE_CURRENT);
        notificationRepository.setAlarmManager(nextBirthday, pendingIntent);
    }

    public void cancelBirthdayAlarm(Context context, int id, String text) {
        Intent intent = intentManager.getIntent(id, text);
        if(notificationRepository.alarmIsUp(context, id, intent, FLAG_NO_CREATE)) {
            PendingIntent pendingIntent = getPendingIntent(context, id, intent, FLAG_UPDATE_CURRENT);
            notificationRepository.cancelAlarmManager(pendingIntent);
        }
    }

    public void notifyNorification(Context context, int id, String text) {
        PendingIntent pendingIntent = getPendingIntent(context, id, intentManager.getIntent(context, MainActivity.class, id), FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.android_icon)
                .setContentText(text)
                .setContentTitle(context.getString(R.string.notification_title))
                .setPriority(PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationRepository.notifyNotification(id, notification.build());
    }

    public boolean alarmIsUp(Context context, int id, String text) {
        return notificationRepository.alarmIsUp(context, id, intentManager.getIntent(id, text), FLAG_NO_CREATE);
    }

    private PendingIntent getPendingIntent(Context context, int id, Intent intent, int flag) {
        return intentManager.getPendingIntent(context, id, intent, flag);
    }
}
