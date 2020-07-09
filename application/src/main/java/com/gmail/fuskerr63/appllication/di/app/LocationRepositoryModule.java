package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.repository.LocationRepositoryImpl;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationRepositoryModule {
    @Singleton
    @Provides
    public LocationRepository provideLocationRepository(AppDatabase appDatabase) {
        return new LocationRepositoryImpl(appDatabase);
    }
}
