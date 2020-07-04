package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.birthday.IntentManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class IntentManagerModule {
    @Singleton
    @Provides
    public IntentManager provideBirthdayNorification() {
        return new IntentManager();
    }
}
