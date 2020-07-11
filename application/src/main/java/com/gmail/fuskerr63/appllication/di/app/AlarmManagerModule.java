package com.gmail.fuskerr63.appllication.di.app;

import android.app.AlarmManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class AlarmManagerModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public AlarmManager provideAlarmManager(@Nullable Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
