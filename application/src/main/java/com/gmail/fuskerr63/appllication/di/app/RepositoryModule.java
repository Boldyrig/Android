package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.repository.Repository;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    private ContactRepository repository;

    public RepositoryModule(Context context) {
        repository = new Repository(context.getContentResolver());
    }

    @Singleton
    @Provides
    public ContactRepository provideRepository() {
        return repository;
    }
}
