package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.repository.ListRepository;
import com.gmail.fuskerr63.java.repository.ContactListRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class RepositoryModule {

    @NonNull
    @Singleton
    @Provides
    public ContactListRepository provideRepository(@NonNull Context context) {
        return new ListRepository(context.getContentResolver());
    }
}
