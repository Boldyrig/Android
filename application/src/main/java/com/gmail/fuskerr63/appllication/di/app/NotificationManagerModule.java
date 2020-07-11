package com.gmail.fuskerr63.appllication.di.app;

import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class NotificationManagerModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public NotificationManager provideNotificationManager(@Nullable Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
