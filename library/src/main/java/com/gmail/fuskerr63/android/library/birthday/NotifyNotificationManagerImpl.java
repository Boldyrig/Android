package com.gmail.fuskerr63.android.library.birthday;

import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class NotifyNotificationManagerImpl implements NotifyNotificationManager {
    @NonNull
    private final NotificationRepository notificationRepository;

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private final int flagUpdateCurrent;
    private final int priority;

    public NotifyNotificationManagerImpl(
            @NonNull NotificationRepository notificationRepository,
            int flagUpdateCurrent,
            int priority) {
        this.notificationRepository = notificationRepository;
        this.flagUpdateCurrent = flagUpdateCurrent;
        this.priority = priority;
    }


    @Override
    public void notifyNotification(int id, @Nullable String text) {
        notificationRepository.notifyNotification(id, text, flagUpdateCurrent, CHANNEL_ID, priority);
    }
}
