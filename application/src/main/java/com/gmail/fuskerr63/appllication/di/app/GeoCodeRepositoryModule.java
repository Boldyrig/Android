package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.android.library.repository.GeoCodeRepositoryImpl;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class GeoCodeRepositoryModule {

    @Nullable
    @Singleton
    @Provides
    public GeoCodeRepository provideGeoCodeRepository(@Nullable GeoCodeRetrofit geoCodeRetrofit) {
        return new GeoCodeRepositoryImpl(geoCodeRetrofit);
    }
}
