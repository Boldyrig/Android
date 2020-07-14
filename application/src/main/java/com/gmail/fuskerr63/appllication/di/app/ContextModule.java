package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class ContextModule {
    @NonNull
    private final Context context;

    public ContextModule(@NonNull Context context) {
        this.context = context;
    }


    @NonNull
    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }
}
