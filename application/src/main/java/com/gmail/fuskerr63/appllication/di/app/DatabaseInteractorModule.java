package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.java.interactor.DatabaseModel;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class DatabaseInteractorModule {

    @Nullable
    @Singleton
    @Provides
    public DatabaseInteractor provideDatabase(@Nullable LocationRepository locationRepository) {
        return new DatabaseModel(locationRepository);
    }
}
