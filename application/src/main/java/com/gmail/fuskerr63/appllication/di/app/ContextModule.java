package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class ContextModule {
    private final transient Context context;

    public ContextModule(@NonNull Context context) {
        this.context = context;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }
}
