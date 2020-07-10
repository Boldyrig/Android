package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.android.library.repository.GeoCodeRepositoryImpl;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeoCodeRepositoryModule {
    @Singleton
    @Provides
    public GeoCodeRepository provideGeoCodeRepository(GeoCodeRetrofit geoCodeRetrofit) {
        return new GeoCodeRepositoryImpl(geoCodeRetrofit);
    }
}
