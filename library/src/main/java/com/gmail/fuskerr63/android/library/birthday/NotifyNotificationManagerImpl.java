package com.gmail.fuskerr63.android.library.birthday;

import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import io.reactivex.annotations.Nullable;

public class NotifyNotificationManagerImpl implements NotifyNotificationManager {
    private transient final NotificationRepository notificationRepository;

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private transient final int flagUpdateCurrent;
    private transient final int priority;

    public NotifyNotificationManagerImpl(
            @Nullable NotificationRepository notificationRepository,
            int flagUpdateCurrent,
            int priority) {
        this.notificationRepository = notificationRepository;
        this.flagUpdateCurrent = flagUpdateCurrent;
        this.priority = priority;
    }

    @SuppressWarnings("unused")
    @Override
    public void notifyNotification(int id, @Nullable String text) {
        notificationRepository.notifyNotification(id, text, flagUpdateCurrent, CHANNEL_ID, priority);
    }
}
