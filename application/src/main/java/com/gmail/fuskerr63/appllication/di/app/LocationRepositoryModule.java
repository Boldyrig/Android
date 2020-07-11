package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.repository.LocationRepositoryImpl;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class LocationRepositoryModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public LocationRepository provideLocationRepository(@Nullable AppDatabase appDatabase) {
        return new LocationRepositoryImpl(appDatabase);
    }
}
