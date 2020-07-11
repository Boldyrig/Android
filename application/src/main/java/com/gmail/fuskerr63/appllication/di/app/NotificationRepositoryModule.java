package com.gmail.fuskerr63.appllication.di.app;

import android.app.AlarmManager;
import android.app.NotificationManager;

import com.gmail.fuskerr63.android.library.birthday.IntentManager;
import com.gmail.fuskerr63.android.library.birthday.NotificationRepositoryImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class NotificationRepositoryModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public NotificationRepository provideNotificationRepository(
            @Nullable AlarmManager alarmManager,
            @Nullable NotificationManager notificationManager,
            @Nullable IntentManager intentManager) {
        return new NotificationRepositoryImpl(alarmManager, notificationManager, intentManager);
    }
}
