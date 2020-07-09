package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.GeoCodeInteractor;
import com.gmail.fuskerr63.java.interactor.GeoCodeModel;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeoCodeInteractorModule {
    @Singleton
    @Provides
    public GeoCodeInteractor provideGeoCodeInteractor(GeoCodeRepository geoCodeRepository) {
        return new GeoCodeModel(geoCodeRepository);
    }
}
