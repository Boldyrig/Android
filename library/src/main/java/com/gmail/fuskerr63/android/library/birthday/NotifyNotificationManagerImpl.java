package com.gmail.fuskerr63.android.library.birthday;

import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

public class NotifyNotificationManagerImpl implements NotifyNotificationManager {
    private NotificationRepository notificationRepository;

    private final String CHANNEL_ID = "CHANNEL_ID";
    private final int FLAG_UPDATE_CURRENT;
    private final int PRIORTY;

    public NotifyNotificationManagerImpl(NotificationRepository notificationRepository, int FLAG_UPDATE_CURRENT, int PRIORTY) {
        this.notificationRepository = notificationRepository;
        this.FLAG_UPDATE_CURRENT = FLAG_UPDATE_CURRENT;
        this.PRIORTY = PRIORTY;
    }

    @Override
    public void notifyNotification(int id, String text) {
        notificationRepository.notifyNotification(id, text, FLAG_UPDATE_CURRENT, CHANNEL_ID, PRIORTY);
    }
}
