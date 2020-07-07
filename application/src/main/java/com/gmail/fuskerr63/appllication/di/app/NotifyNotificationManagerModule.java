package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.birthday.NotifyNotificationManagerImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotifyNotificationManagerModule {
    private final int FLAG_UPDATE_CURRENT;
    private final int PRIORTY;

    public NotifyNotificationManagerModule(int FLAG_UPDATE_CURRENT, int PRIORTY) {
        this.FLAG_UPDATE_CURRENT = FLAG_UPDATE_CURRENT;
        this.PRIORTY = PRIORTY;
    }

    @Singleton
    @Provides
    public NotifyNotificationManager provideNotifyNotificationManager(NotificationRepository notificationRepository) {
        return new NotifyNotificationManagerImpl(notificationRepository, FLAG_UPDATE_CURRENT, PRIORTY);
    }
}
