package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.NotificationTime;
import com.gmail.fuskerr63.java.interactor.NotificationTimeImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class NotificationTimeModule {

    @Nullable
    @Singleton
    @Provides
    public NotificationTime provideNotificationTime() {
        return new NotificationTimeImpl();
    }
}
