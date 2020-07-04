package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.birthday.IntentManager;
import com.gmail.fuskerr63.android.library.birthday.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BirthdayNotificationModule {
    @Singleton
    @Provides
    public BirthdayNotification provideBirthdayNorification(NotificationRepository notificationRepository, NotificationInteractor notificationInteractor, IntentManager intentManager) {
        return new BirthdayNotification(notificationRepository, notificationInteractor, intentManager);
    }
}
