package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.android.library.network.interactor.GeoCodeInteractor;
import com.gmail.fuskerr63.android.library.network.interactor.GeoCodeModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeoCodeInteractorModule {
    @Singleton
    @Provides
    public GeoCodeInteractor provideGeoCodeInteractor(GeoCodeRetrofit geoCodeRetrofit) {
        return new GeoCodeModel(geoCodeRetrofit);
    }
}
