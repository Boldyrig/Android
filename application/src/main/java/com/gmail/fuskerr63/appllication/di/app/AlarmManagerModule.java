package com.gmail.fuskerr63.appllication.di.app;

import android.app.AlarmManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AlarmManagerModule {
    @Singleton
    @Provides
    public AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    }
}
