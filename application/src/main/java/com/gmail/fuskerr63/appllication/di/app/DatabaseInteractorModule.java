package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.database.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.database.interactor.DatabaseModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseInteractorModule {
    @Singleton
    @Provides
    public DatabaseInteractor provideDatabase(AppDatabase appDatabase) {
        return new DatabaseModel(appDatabase);
    }
}
