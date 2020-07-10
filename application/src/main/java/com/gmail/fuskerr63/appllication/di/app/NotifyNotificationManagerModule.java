package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.birthday.NotifyNotificationManagerImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotifyNotificationManagerModule {
    private final int flagUpdateCurrent;
    private final int priority;

    public NotifyNotificationManagerModule(int flagUpdateCurrent, int priority) {
        this.flagUpdateCurrent = flagUpdateCurrent;
        this.priority = priority;
    }

    @Singleton
    @Provides
    public NotifyNotificationManager provideNotifyNotificationManager(NotificationRepository notificationRepository) {
        return new NotifyNotificationManagerImpl(notificationRepository, flagUpdateCurrent, priority);
    }
}
