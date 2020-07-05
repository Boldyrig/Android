package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.birthday.IntentManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class IntentManagerModule {
    @Singleton
    @Provides
    public IntentManager provideBirthdayNorification(Context context) {
        return new IntentManager(context);
    }
}
