package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.GeoCodeInteractor;
import com.gmail.fuskerr63.java.interactor.GeoCodeModel;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class GeoCodeInteractorModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public GeoCodeInteractor provideGeoCodeInteractor(@Nullable GeoCodeRepository geoCodeRepository) {
        return new GeoCodeModel(geoCodeRepository);
    }
}
