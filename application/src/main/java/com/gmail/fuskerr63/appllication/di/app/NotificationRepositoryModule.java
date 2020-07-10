package com.gmail.fuskerr63.appllication.di.app;

import android.app.AlarmManager;
import android.app.NotificationManager;

import com.gmail.fuskerr63.android.library.birthday.IntentManager;
import com.gmail.fuskerr63.android.library.birthday.NotificationRepositoryImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationRepositoryModule {
    @Singleton
    @Provides
    public NotificationRepository provideNotificationRepository(AlarmManager alarmManager, NotificationManager notificationManager, IntentManager intentManager) {
        return new NotificationRepositoryImpl(alarmManager, notificationManager, intentManager);
    }
}
