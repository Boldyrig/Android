package com.gmail.fuskerr63.appllication.di.app;

import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class NotificationManagerModule {

    @Nullable
    @Singleton
    @Provides
    public NotificationManager provideNotificationManager(@Nullable Context context) {
        if (context != null) {
            return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return null;
    }
}
