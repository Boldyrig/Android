package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.java.interactor.DatabaseModel;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseInteractorModule {
    @Singleton
    @Provides
    public DatabaseInteractor provideDatabase(LocationRepository locationRepository) {
        return new DatabaseModel(locationRepository);
    }
}
