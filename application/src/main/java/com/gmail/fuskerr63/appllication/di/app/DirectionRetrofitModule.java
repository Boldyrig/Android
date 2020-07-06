package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DirectionRetrofitModule {
    @Singleton
    @Provides
    public DirectionRetrofit provideDirectionRetrofit() {
        return new DirectionRetrofit();
    }
}
