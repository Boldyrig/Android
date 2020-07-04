package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RetrofitModule {
    @Singleton
    @Provides
    public GeoCodeRetrofit provideRetrofit() {
        return new GeoCodeRetrofit();
    }
}
