package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.NotificationTime;
import com.gmail.fuskerr63.java.interactor.NotificationTimeImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationTimeModule {
    @Singleton
    @Provides
    public NotificationTime provideNotificationTime() {
        return new NotificationTimeImpl();
    }
}
