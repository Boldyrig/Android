package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class DirectionRetrofitModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public DirectionRetrofit provideDirectionRetrofit() {
        return new DirectionRetrofit();
    }
}
