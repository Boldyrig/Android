package com.gmail.fuskerr63.di.app;

import android.content.Context;

import com.gmail.fuskerr63.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    private Repository repository;

    public RepositoryModule(Context context) {
        repository = new Repository(context.getContentResolver());
    }

    @Singleton
    @Provides
    public Repository provideRepository() {
        return repository;
    }
}
