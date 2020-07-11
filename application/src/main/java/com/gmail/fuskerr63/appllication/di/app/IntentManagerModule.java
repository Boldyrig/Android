package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.android.library.birthday.IntentManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class IntentManagerModule {
    private final Class<MainActivity> mainActivityClass;

    public IntentManagerModule(@Nullable Class<MainActivity> mainActivityClass) {
        this.mainActivityClass = mainActivityClass;
    }

    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public IntentManager provideBirthdayNorification(@Nullable Context context) {
        return new IntentManager(mainActivityClass, context);
    }
}
