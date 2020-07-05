package com.gmail.fuskerr63.appllication.di.app;

import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationManagerModule {
    @Singleton
    @Provides
    public NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
    }
}
