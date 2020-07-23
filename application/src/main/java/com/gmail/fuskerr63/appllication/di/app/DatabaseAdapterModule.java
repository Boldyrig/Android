package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.DatabaseAdapter;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class DatabaseAdapterModule {
    @NonNull
    @Singleton
    @Provides
    public DatabaseAdapter provideDatabaseAdapter(@NonNull DatabaseInteractor databaseInteractor) {
        return new DatabaseAdapter(databaseInteractor);
    }
}
