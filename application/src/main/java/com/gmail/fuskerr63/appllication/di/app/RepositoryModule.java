package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.repository.Repository;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class RepositoryModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public ContactRepository provideRepository(@Nullable Context context) {
        return new Repository(context.getContentResolver());
    }
}
