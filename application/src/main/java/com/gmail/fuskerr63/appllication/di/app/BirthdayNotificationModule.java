package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BirthdayNotificationModule {
    @Singleton
    @Provides
    public BirthdayNotification provideBirthdayNorification(NotificationInteractor notificationInteractor) {
        return new BirthdayNotification(notificationInteractor);
    }
}
