package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class ContextModule {
    private final Context context;

    public ContextModule(@Nullable Context context) {
        this.context = context;
    }

    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }
}
