package com.gmail.fuskerr63.appllication.di.app;

import android.app.AlarmManager;
import android.app.NotificationManager;

import com.gmail.fuskerr63.android.library.birthday.NotificationRepository;
import com.gmail.fuskerr63.android.library.birthday.NotificationRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationRepositoryModule {
    private NotificationRepository notificationRepository;

    public NotificationRepositoryModule(AlarmManager alarmManager, NotificationManager notificationManager) {
        notificationRepository = new NotificationRepositoryImpl(alarmManager, notificationManager);
    }

    @Singleton
    @Provides
    public NotificationRepository provideNotificationRepository() {
        return notificationRepository;
    }
}
